package semios.api.service.chain;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import semios.api.model.dto.response.TransactionDto;
import semios.api.model.entity.Canvas;
import semios.api.model.entity.Dao;
import semios.api.model.enums.DaoStatusEnum;
import semios.api.service.ICanvasService;
import semios.api.service.IDaoService;
import semios.api.service.SubscriberChainService;
import semios.api.service.common.CommonService;
import semios.api.utils.CommonUtil;
import semios.api.utils.JacksonUtil;

import java.math.BigDecimal;
import java.util.List;

/**
 * basic DAO 解锁事件
 *
 * @description:
 * @author: xiangbin
 * @create: 2023-09-04 13:43
 **/
@Slf4j
@Service
public class DaoRemainingRoundSetChainService implements SubscriberChainService {

    @Autowired
    private IDaoService daoService;


    @Autowired
    private CommonService commonService;

    @Autowired
    private ICanvasService canvasService;

    public static void main(String[] args) {
        String data = "58d2d3c7a28441df4656fab3eb0e51d94a45b38100a7e74dba50afae6124d6d72ae972c0263f681f4f35a5fbffcb8860ddb2ceb32ab4e3a4d8f6912888dca8c8000000000000000000000000ffd23ddffa1d6181c8a711a8b4939eedf9cc00bd0000000000000000000000000000000000000000000000000000000000000000";
        List<String> dataList = CommonUtil.splitBy32Bytes(data);


        String projectId = dataList.get(0);
        String canvasId = dataList.get(1);
        String erc20Token = CommonUtil.addHexPrefixIfNotExist(CommonUtil.formatBytes32Address(dataList.get(2)));
        String amount = CommonUtil.hexToTenString(dataList.get(3));
        System.out.println("project_id" + " is " + projectId);
        System.out.println("canvas_id" + " is " + canvasId);
        System.out.println("erc20_token" + " is " + erc20Token);
        System.out.println("amount" + " is " + amount);
    }

    @Override
    public void handleTrade(TransactionDto transactionDto) throws Exception {

        log.info("[DaoRemainingRoundSetChainService] transactionDao:{}", JacksonUtil.obj2json(transactionDto));

        String data = CommonUtil.removeHexPrefixIfExists(transactionDto.getData());
        List<String> dataList = CommonUtil.splitBy32Bytes(data);

        String projectId = CommonUtil.removeHexPrefixIfExists(dataList.get(0));
        String remainingRound = CommonUtil.hexToTenString(dataList.get(1));

        Dao dao = daoService.daoDetailByProjectId(projectId);
        if (dao == null) {
            throw new RuntimeException("DaoRemainingRoundSetChainService cannot find dao");
        }


        Dao updateDao = new Dao();
        updateDao.setId(dao.getId());
        if (DaoStatusEnum.FINISHED.getStatus().equals(dao.getDaoStatus())) {
            updateDao.setDaoStatus(DaoStatusEnum.STARTED.getStatus());
            //重新开启后计算canvas价格
            // 更新所有canvas的最近铸造价格
            commonService.updateCanvasPrice(Integer.valueOf(dao.getCurrentRound()) + 1, dao.getId());

            if (dao.getGlobalDaoPrice() != null && dao.getGlobalDaoPrice().compareTo(BigDecimal.ZERO) >= 0) {
                updateDao.setCanvasFloorPrice(dao.getGlobalDaoPrice());
            } else {
                Canvas canvas = canvasService.listCanvasFloorPriceByDaoId(dao.getId() + "");
                if (canvas != null && canvas.getCurrentPrice() != null) {
                    log.info("[DaoRemainingRoundSetChainService] daoId:{} price:{}", dao.getId(), canvas.getCurrentPrice());
                    if (canvas.getCurrentPrice().compareTo(dao.getCanvasFloorPrice()) != 0) {
                        log.info("[DaoRemainingRoundSetChainService] price is changed daoId:{} originPrice:{} price:{}", dao.getId(),
                                dao.getCanvasFloorPrice(), canvas.getCurrentPrice());
                        updateDao.setCanvasFloorPrice(canvas.getCurrentPrice());

                    }
                }
            }
        }
        updateDao.setDaoMintWindow(Integer.valueOf(remainingRound));
        updateDao.setRemainingMintWindow(Integer.valueOf(remainingRound));
        daoService.updateById(updateDao);
        log.info("[DaoRemainingRoundSetChainService] daoId:{} remainingRound:{}", dao.getId(), remainingRound);

    }
}

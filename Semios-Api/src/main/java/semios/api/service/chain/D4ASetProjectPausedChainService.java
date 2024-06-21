package semios.api.service.chain;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import semios.api.model.dto.common.ProtoDaoConstant;
import semios.api.model.dto.response.TransactionDto;
import semios.api.model.entity.Canvas;
import semios.api.model.entity.Dao;
import semios.api.model.entity.ShutdownRecord;
import semios.api.model.enums.DaoStatusEnum;
import semios.api.model.enums.ShutdownTypeEnum;
import semios.api.service.ICanvasService;
import semios.api.service.IDaoService;
import semios.api.service.SubscriberChainService;
import semios.api.utils.CommonUtil;
import semios.api.utils.JacksonUtil;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DAO停机事件
 *
 * @description: DAO停机事件
 * @author: xiangbin
 * @create: 2022-08-25 13:43
 **/
@Slf4j
@Service
public class D4ASetProjectPausedChainService implements SubscriberChainService {


    @Autowired
    private IDaoService daoService;

    @Autowired
    private ICanvasService canvasService;

    public static void main(String[] args) {
        String data = "58d2d3c7a28441df4656fab3eb0e51d94a45b38100a7e74dba50afae6124d6d72ae972c0263f681f4f35a5fbffcb8860ddb2ceb32ab4e3a4d8f6912888dca8c8000000000000000000000000ffd23ddffa1d6181c8a711a8b4939eedf9cc00bd0000000000000000000000000000000000000000000000000000000000000000";
        List<String> dataList = CommonUtil.splitBy32Bytes(data);


        String project_id = dataList.get(0);
        String canvas_id = dataList.get(1);
        String erc20_token = CommonUtil.addHexPrefixIfNotExist(CommonUtil.formatBytes32Address(dataList.get(2)));
        String amount = CommonUtil.hexToTenString(dataList.get(3));
        System.out.println("project_id" + " is " + project_id);
        System.out.println("canvas_id" + " is " + canvas_id);
        System.out.println("erc20_token" + " is " + erc20_token);
        System.out.println("amount" + " is " + amount);
    }

    @Override
    public void handleTrade(TransactionDto transactionDto) throws Exception {

        log.info("[D4ASetProjectPausedChainService] transactionDao:{}", JacksonUtil.obj2json(transactionDto));
        String data = CommonUtil.removeHexPrefixIfExists(transactionDto.getData());
        List<String> dataList = CommonUtil.splitBy32Bytes(data);
        String obj_id = dataList.get(0);
        String is_paused = CommonUtil.hexToTenString(dataList.get(1));

        Dao dao = daoService.daoDetailByProjectId(obj_id);
        if (dao == null) {
            throw new RuntimeException("D4ASetProjectPausedChainService cannot find dao");
        }

        ShutdownRecord shutdownRecord = new ShutdownRecord();
        shutdownRecord.setType(ShutdownTypeEnum.DAO.getType());
        shutdownRecord.setShutdownId(CommonUtil.addHexPrefixIfNotExist(obj_id));
        shutdownRecord.setRecordId(dao.getId());
        shutdownRecord.setIsPaused("1".equals(is_paused) ? 0 : 1);//是否停机,true停机，false不停机
        shutdownRecord.setBlockNumber(transactionDto.getBlockNumber());
        shutdownRecord.setBlockTime(transactionDto.getBlockTime());
        shutdownRecord.setDrbNumber(Integer.valueOf(ProtoDaoConstant.CURRENT_ROUND));
        shutdownRecord.setTransactionHash(transactionDto.getTransactionHash());
        shutdownRecord.setCreateTime(LocalDateTime.now());

        List<Canvas> canvasList = canvasService.listCanvasByDaoId(String.valueOf(dao.getId()));

        if ("1".equals(is_paused)) {
            dao.setDaoStatus(DaoStatusEnum.SHUT_DOWN.getStatus());
            canvasList.stream().forEach(v -> v.setDaoStatus(DaoStatusEnum.SHUT_DOWN.getStatus()));
        } else {
            dao.setDaoStatus(DaoStatusEnum.STARTED.getStatus());
            canvasList.stream().forEach(v -> v.setDaoStatus(DaoStatusEnum.STARTED.getStatus()));
        }


        int i = daoService.updateDaoPaused(dao, shutdownRecord, canvasList);
        if (i != canvasList.size() + 2) {
            log.error("[D4ASetProjectPausedChainService] update dao size:{} error daoId:{}", i, dao.getId());
        }

    }
}

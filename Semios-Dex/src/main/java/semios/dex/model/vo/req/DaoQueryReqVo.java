package semios.dex.model.vo.req;

import lombok.Data;

/**
 * 按照7天，30天，90天查询信息参数
 *
 * @description:
 * @author: xiangbin
 * @create: 2023-04-19 15:14
 **/
@Data
public class DaoQueryReqVo {

    /**
     * dao id
     */
    private Integer daoId;

    /**
     * 查询条件 7天-30天-90天
     *
     * @mock 7
     */
    private Integer dayTime;

}

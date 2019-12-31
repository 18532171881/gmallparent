package com.zjh.gmall.to.es;

import com.zjh.gmall.pms.entity.SkuStock;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Created by 24923 on 2019/12/31 11:00
 */
@Data
public class EsSkuProductInfo extends SkuStock implements Serializable {

    private String skuTitle;//sku的特定标题
    /*
    * 每个sku不同的属性以及他的值
    * */
    List<EsProductAttributeValue> attributeValues;


}

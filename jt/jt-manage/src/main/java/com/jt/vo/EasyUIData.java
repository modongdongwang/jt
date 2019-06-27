package com.jt.vo;
import java.io.Serializable;
import java.util.List;
import com.jt.pojo.Item;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
/**
 * 该类展现表格数据
 */
@Data
@Accessors
@NoArgsConstructor
@AllArgsConstructor
public class EasyUIData implements Serializable{
	private Integer total;//记录总数
	private List<Item> rows;//展现数据集合
}

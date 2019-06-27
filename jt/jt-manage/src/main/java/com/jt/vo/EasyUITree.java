package com.jt.vo;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain=true)
public class EasyUITree {
	@TableId
	private Long id;//节点id值
	private String text;//名称
	private String state;//close,open
}

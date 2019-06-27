package com.jt.vo;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain=true)
public class ImageVO implements Serializable{
	private Integer error;
	private String url;
	private Integer width;
	private Integer height;
}

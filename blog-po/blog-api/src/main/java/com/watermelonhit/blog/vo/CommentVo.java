package com.watermelonhit.blog.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.util.List;

/**
 * @Author watermelonhit
 * @DateTime 2021/8/27
 */
@Data
public class CommentVo  {
    //分布式id比较长，容易导致精度损失
    //转成json返回给前端，防止精度损失
//    @JsonSerialize(using = ToStringSerializer.class)
    private String  id;

    private UserVo author;

    private String content;

    private List<CommentVo> childrens;

    private String createDate;

    private Integer level;

    private UserVo toUser;
}
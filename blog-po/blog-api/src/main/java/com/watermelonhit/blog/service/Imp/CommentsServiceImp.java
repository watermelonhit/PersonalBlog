package com.watermelonhit.blog.service.Imp;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.watermelonhit.blog.dao.Article;
import com.watermelonhit.blog.dao.Comment;
import com.watermelonhit.blog.dao.SysUser;
import com.watermelonhit.blog.mapper.ArticleMapper;
import com.watermelonhit.blog.mapper.CommentMapper;
import com.watermelonhit.blog.service.ArticleService;
import com.watermelonhit.blog.service.CommentsService;
import com.watermelonhit.blog.service.SysUserService;
import com.watermelonhit.blog.service.ThreadService;
import com.watermelonhit.blog.util.UserThreadLocal;
import com.watermelonhit.blog.vo.CommentVo;
import com.watermelonhit.blog.vo.Result;
import com.watermelonhit.blog.vo.UserVo;
import com.watermelonhit.blog.vo.params.CommentParams;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author watermelonhit
 * @DateTime 2021/8/27
 */
@Service
public class CommentsServiceImp implements CommentsService {
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private ArticleMapper articleMapper;
    @Override
    public Result getCommentsByArticleId(Long id) {
        //根据文章id获取评论列表
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getArticleId,id);
        List<Comment> comments = commentMapper.selectList(queryWrapper);
        //将List<Comment> comments转变为List<CommentVo> comments
        List<CommentVo> commentVos=copyList(comments);
        return Result.success(commentVos);
    }

    @Override
    public Result comment(CommentParams commentParam) {
        SysUser sysUser = UserThreadLocal.get();
        Comment comment = new Comment();
        // 根据文章id获取文章，再把评论数加1
        Article article = articleMapper.selectById(commentParam.getArticleId());
        article.setCommentCounts(article.getCommentCounts()+1);
        articleMapper.updateById(article);
        // 处理文章评论
        comment.setArticleId(commentParam.getArticleId());
        comment.setAuthorId(sysUser.getId());
        comment.setContent(commentParam.getContent());
        comment.setCreateDate(System.currentTimeMillis());
        Long parent = commentParam.getParent();
        if (parent == null || parent == 0) {
            comment.setLevel(1);
        }else{
            comment.setLevel(2);
        }
        comment.setParentId(parent == null ? 0 : parent);
        Long toUserId = commentParam.getToUserId();
        comment.setToUid(toUserId == null ? 0 : toUserId);
        //存入数据库
        this.commentMapper.insert(comment);
        return Result.success(null);
    }

    private List<CommentVo> copyList(List<Comment> comments) {
        ArrayList<CommentVo> commentVos = new ArrayList<>();
        for (Comment comment : comments) {
            commentVos.add(copy(comment));
        }
        return commentVos;
    }

    @Autowired
    private SysUserService sysUserService;
    private CommentVo copy(Comment comment) {
        CommentVo commentVo = new CommentVo();
        BeanUtils.copyProperties(comment,commentVo);
        commentVo.setId(String.valueOf(comment.getId()));
        //返回评论者信息
        UserVo userVo=sysUserService.getAuthorVoById(comment.getAuthorId());
        commentVo.setAuthor(userVo);
        commentVo.setId(String.valueOf(comment.getId()));
        Integer level = comment.getLevel();
        //查询子评论
        if(level==1){
            List<CommentVo> commentVos=getChildrenComments(comment.getId());
            commentVo.setChildrens(commentVos);
        }
        //查询是写给谁的,二级评论才有
        if(level>1){
            UserVo toUser=sysUserService.getAuthorVoById(comment.getToUid());
            commentVo.setToUser(toUser);
        }
        return commentVo;
    }

    private List<CommentVo> getChildrenComments(Long id) {
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getParentId,id);
        //确保是二级评论
        queryWrapper.eq(Comment::getLevel,2);
        List<Comment> comments = commentMapper.selectList(queryWrapper);
        //有点小饶，注意是限定了二级评论
        return copyList(comments);
    }
}

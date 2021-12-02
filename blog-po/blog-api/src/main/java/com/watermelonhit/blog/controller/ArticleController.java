package com.watermelonhit.blog.controller;

import com.watermelonhit.blog.common.aop.Cache;
import com.watermelonhit.blog.common.aop.LogAnnotation;
import com.watermelonhit.blog.service.ArticleService;
import com.watermelonhit.blog.vo.Result;
import com.watermelonhit.blog.vo.params.ArticleBodyParam;
import com.watermelonhit.blog.vo.params.ArticleParam;
import com.watermelonhit.blog.vo.params.PageParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

/**
 * @Author watermelonhit
 * @DateTime 2021/8/25
 */
@RestController
@RequestMapping("articles")
public class ArticleController {
    @Autowired
    private ArticleService articleServiceImp;
    /**
     * 首页文章列表(带有tag，author)
     * @param pageParams
     * @return
     */
    //@Cache(expire = 5*60*1000,name="首页文章显示")
    @LogAnnotation(module = "文章",operator = "展示文章列表")
    @PostMapping
    public Result listArticles(@RequestBody PageParams pageParams) {
        return articleServiceImp.listArticles(pageParams);
    }

    /**
     * 返回最热文章（不带author，tag)
     * @return
     */
    @Cache(expire = 5*60*1000,name="首页返回最热文章")
    @PostMapping("/hot")
    public Result getHotArticles(){
        int limit=5;
        return articleServiceImp.getHotArticles(limit);
    }

    /**
     * 首页 最新文章
     * @return
     */
    @Cache(expire = 5*60*1000,name="首页返回最新文章")
    @PostMapping("/new")
    public Result getNewArticles(){
        int limit=5;
        return articleServiceImp.getNewArticles(limit);
    }

    /**
     * 首页归档
     * @return
     */
    @Cache(expire = 5*60*1000,name="首页归档")
    @PostMapping("/listArchives")
    public Result listArchives(){
        return articleServiceImp.listArchives();
    }

    /**
     * 文章详情
     * @param id
     * @return
     */

    @PostMapping("/view/{id}")
    public Result getArticleById(@PathVariable("id")Long id){
        return articleServiceImp.getArticleById(id);
    }

    /**
     * 编辑文章所用
     * @param id
     * @return
     */
    @PostMapping("/{id}")
    public Result getArticleByIdToEdit(@PathVariable("id")Long id){
        return articleServiceImp.getArticleById(id);
    }

    /**
     * 发布文章
     */
    //利用aop进行打印日志
    @Transactional
    @LogAnnotation(module = "文章",operator = "发表文章")
    @PostMapping("/publish")
    public Result publish(@RequestBody ArticleParam articleParam){
        return articleServiceImp.publish(articleParam);
    }
}

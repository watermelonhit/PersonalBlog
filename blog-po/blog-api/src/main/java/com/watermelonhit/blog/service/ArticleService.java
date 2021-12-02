package com.watermelonhit.blog.service;

import com.watermelonhit.blog.vo.Result;
import com.watermelonhit.blog.vo.params.ArticleParam;
import com.watermelonhit.blog.vo.params.PageParams;
import org.springframework.stereotype.Service;

/**
 * @Author watermelonhit
 * @DateTime 2021/8/25
 */

public interface ArticleService {

    public Result listArticles(PageParams pageParams);

    Result getHotArticles(int limit);

    Result getNewArticles(int limit);

    Result listArchives();

    Result getArticleById(Long id);

    Result publish(ArticleParam articleParam);
}

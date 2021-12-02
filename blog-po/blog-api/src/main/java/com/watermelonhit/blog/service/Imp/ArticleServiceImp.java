package com.watermelonhit.blog.service.Imp;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.watermelonhit.blog.dao.*;
import com.watermelonhit.blog.mapper.*;
import com.watermelonhit.blog.service.*;
import com.watermelonhit.blog.util.UserThreadLocal;
import com.watermelonhit.blog.vo.ArticleBodyVo;
import com.watermelonhit.blog.vo.ArticleVo;
import com.watermelonhit.blog.vo.Result;
import com.watermelonhit.blog.vo.TagVo;
import com.watermelonhit.blog.vo.params.ArticleParam;
import com.watermelonhit.blog.vo.params.PageParams;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @Author watermelonhit
 * @DateTime 2021/8/25
 */
@Service
public class ArticleServiceImp implements ArticleService {
    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private TagService tagServiceImp;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private ArchiveMapper archiveMapper;
    @Autowired
    private ArticleBodyMapper articleBodyMapper;
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private CategoryService categoryService;
    /**
     * 首页展示文章（带有tag，author)
     * @param pageParams
     * @return
     */
    @Override
    public Result listArticles(PageParams pageParams) {
//        Page<Article> articlePage = new Page<Article>(pageParams.getPage(),pageParams.getPageSize());
//        LambdaQueryWrapper<Article> articleLambdaQueryWrapper = new LambdaQueryWrapper<>();
//        Long pageParamsCategoryId = pageParams.getCategoryId();
//        if(pageParamsCategoryId!=null){
//            articleLambdaQueryWrapper.eq(Article::getCategoryId,pageParamsCategoryId);
//        }
//        Long tagId = pageParams.getTagId();
//        //根据tagId返回所有文章
//        ArrayList<Long> articleIds= new ArrayList<>();
//        if(tagId!=null){
//            LambdaQueryWrapper<ArticleTag> queryWrapper = new LambdaQueryWrapper<>();
//            queryWrapper.eq(ArticleTag::getTagId, tagId);
//            List<ArticleTag> articleTags = articleTagMapper.selectList(queryWrapper);
//            if(articleTags!=null&&articleTags.size()>0){
//                for (ArticleTag articleTag : articleTags) {
//                    articleIds.add(articleTag.getArticleId());
//                }
//                articleLambdaQueryWrapper.in(Article::getId,articleIds);
//            }
//        }
//        articleLambdaQueryWrapper.orderByDesc(Article::getWeight,Article::getCreateDate);
//        articleMapper.selectPage(articlePage,articleLambdaQueryWrapper);
//        List<Article> records = articlePage.getRecords();
//        List<ArticleVo> articleVoList=copyList(records,true,true);
//        return Result.success(articleVoList);
        //上面太复杂，使用自定义sql语句实现

        Page<Article> articlePage = new Page<Article>(pageParams.getPage(),pageParams.getPageSize());
        IPage<Article>iPage=articleMapper.listArticle(
                articlePage,
                pageParams.getCategoryId(),
                pageParams.getTagId(),
                pageParams.getYear(),
                pageParams.getMonth()
            );
        return Result.success(copyList(iPage.getRecords(),true,true));
    }

    /**
     * 返回最热文章（不带author，tag)
     * @param limit
     * @return
     */
    @Override
    public Result getHotArticles(int limit) {
        LambdaQueryWrapper<Article> articleLambdaQueryWrapper = new LambdaQueryWrapper<>();
        articleLambdaQueryWrapper.orderByDesc(Article::getViewCounts );
        articleLambdaQueryWrapper.select(Article::getId,Article::getTitle);
        articleLambdaQueryWrapper.last("limit "+limit);
        List<Article> articleList = articleMapper.selectList(articleLambdaQueryWrapper);
        return Result.success(copyList(articleList,false,false));
    }

    @Override
    public Result getNewArticles(int limit) {
        LambdaQueryWrapper<Article> articleLambdaQueryWrapper = new LambdaQueryWrapper<>();
        articleLambdaQueryWrapper.orderByDesc(Article::getCreateDate );
        articleLambdaQueryWrapper.select(Article::getId,Article::getTitle);
        articleLambdaQueryWrapper.last("limit "+limit);
        List<Article> articleList = articleMapper.selectList(articleLambdaQueryWrapper);
        return Result.success(copyList(articleList,false,false));
    }

    @Override
    public Result listArchives() {
        return Result.success(archiveMapper.listArchives());
    }

    @Autowired
    private ThreadService threadService;
    //首页点击文章详情后，获取文章内容和分类
    @Override
    public Result getArticleById(Long id) {
        Article article = articleMapper.selectById(id);
        ArticleVo articleVo = copy(article, true, true,true,true);
         //阅读完成后（而不是立马加1，所以传入article），异步处理阅读数加一（开启多线程）
        threadService.updateArticleViewCount(articleMapper,article);
        return Result.success(articleVo);
    }
    @Autowired
    private ArticleTagMapper articleTagMapper;
    @Autowired
    private TagMapper tagMapper;
    /**
     * 发布文章
     * 更新：加入了动态标签
     * @param articleParam
     * @return
     */
    @Override
    public Result publish(ArticleParam articleParam) {
        List<TagVo> tags = articleParam.getTags();
        List<String> dynamicTags = articleParam.getDynamicTags();
        // 从数据库中获取所有的tags,进行去重后保存到数据库中
        LambdaQueryWrapper<Tag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(Tag::getTagName);
        List<Tag> tagList = tagMapper.selectList(queryWrapper);
        for (String dynamicTag : dynamicTags) {
            boolean judge=false;
            for (Tag tag : tagList) {
                if(dynamicTag.equals(tag.getTagName())){
                    judge=true;
                    break;
                }
            }
            if(judge==true){
                continue;
            }
            Tag tag = new Tag();
            tag.setTagName(dynamicTag);
            tag.setAvatar("/static/tag/yu.png");
            tagMapper.insert(tag);
            //对该文章进行tags绑定
            TagVo tagVo = new TagVo();
            if(tag.getId()!=null){
                tagVo.setId(String.valueOf(tag.getId()));
            }
            tags.add(tagVo);
        }
        SysUser sysUser = UserThreadLocal.get();
        Article article = new Article();
        article.setAuthorId(sysUser.getId());
        article.setCreateDate(System.currentTimeMillis());
        article.setCategoryId(Long.parseLong(articleParam.getCategory().getId()));
        article.setSummary(articleParam.getSummary());
        article.setWeight(Article.Article_Common);
        article.setViewCounts(0);
        article.setCommentCounts(0);
        article.setTitle(articleParam.getTitle());
        //插入数据库后，主键自增，会返回一个值到对象中
        articleMapper.insert(article);
        //遍历获取所有标签,并且插入ArticleTag表中

        if(tags!=null){
            for (TagVo tag : tags) {
                ArticleTag articletag = new ArticleTag();
                articletag.setArticleId(article.getId());
                articletag.setTagId(Long.parseLong(tag.getId()));
                articleTagMapper.insert(articletag);
            }
        }
        //把articleBody出入数据库中
        ArticleBody articleBody = new ArticleBody();
        articleBody.setArticleId(article.getId());
        articleBody.setContent(articleParam.getBody().getContent());
        articleBody.setContentHtml(articleParam.getBody().getContentHtml());
        articleBodyMapper.insert(articleBody);
        //更新article
        article.setBodyId(articleBody.getId());
        articleMapper.updateById(article);
        HashMap<String, String> map = new HashMap<>();
        map.put("id", article.getId().toString());
        return Result.success(map);
    }

    private List<ArticleVo> copyList(List<Article> records,boolean isTag,boolean isAuthor) {
        List<ArticleVo> articleVos = new ArrayList<>();
        for (Article record : records) {
            articleVos.add(copy(record,isTag,isAuthor,false,false));
        }
        return articleVos;
    }
    private List<ArticleVo> copyList(List<Article> records,boolean isTag,boolean isAuthor,boolean isArticleBody,boolean isCategory) {
        List<ArticleVo> articleVos = new ArrayList<>();
        for (Article record : records) {
            articleVos.add(copy(record,isTag,isAuthor,isArticleBody,isCategory));
        }
        return articleVos;
    }
    private ArticleVo copy(Article article,boolean isTag,boolean isAuthor,boolean isArticleBody,boolean isCategory){
        ArticleVo articleVo = new ArticleVo();
        //复制相同的属性
        BeanUtils.copyProperties(article,articleVo);
        articleVo.setId(String.valueOf(article.getId()));
        if(isTag){
            Long id = article.getId();
            articleVo.setTags(tagServiceImp.getTagVoListByArticleId(id));
        }
        if(isAuthor){
            Long authorId = article.getAuthorId();
            articleVo.setAuthor(sysUserService.getSysUserById(authorId).getNickname());
            articleVo.setAccount(sysUserService.getSysUserById(authorId).getAccount());
        }
        if(isArticleBody){
           articleVo.setBody(getArticleBodyVoById(article.getBodyId()));
        }
        if(isCategory){
            Long id = article.getCategoryId();
            articleVo.setCategory(categoryService.getCategoryById(id));
        }
        articleVo.setCreateDate(new DateTime(article.getCreateDate()).toString("yyyy-MM-dd HH:mm"));
        return articleVo;
    }
    private ArticleBodyVo getArticleBodyVoById(long id){
        ArticleBody articleBody = articleBodyMapper.selectById(id);
        ArticleBodyVo articleBodyVo = new ArticleBodyVo();
        articleBodyVo.setContent(articleBody.getContent());
        return articleBodyVo;
    }
}

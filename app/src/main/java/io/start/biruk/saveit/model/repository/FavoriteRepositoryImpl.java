package io.start.biruk.saveit.model.repository;

import io.start.biruk.saveit.model.db.ArticleModel;

/**
 * Created by biruk on 5/22/2018.
 */
public class FavoriteRepositoryImpl implements ArticleRepository {


    public void check(ArticleModel articleModel){
        ArticleModel articleFavModified = new ArticleModel.ArticleBuilder()
                .path(articleModel.getPath())
                .title(articleModel.getTitle())
                .url(articleModel.getUrl())
                .savedDate(articleModel.getSavedDate())
                .isFavorite(!articleModel.isFavorite())     //toggle favoirte
                .build();

        //save it in the db
        
    }
}

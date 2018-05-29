package io.start.biruk.saveit.model.db;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created by biruk on 5/10/2018.
 */
@DatabaseTable(tableName = ArticleModel.ARTICLE_TABLE)
public class ArticleModel implements Serializable {

    public static final String ARTICLE_TABLE = "articles";


    @DatabaseField(id = true)
    private String url;

    @DatabaseField
    private String title;

    @DatabaseField(canBeNull = false)
    private String path;

    @DatabaseField
    private String tags;

    @DatabaseField
    private boolean isFavorite;

    @DatabaseField
    private String savedDate;


    ArticleModel() {
    }

    private ArticleModel(Builder articleBuilder) {
        this.url = articleBuilder.url;
        this.title = articleBuilder.title;
        this.path = articleBuilder.path;
        this.tags = articleBuilder.tags;
        this.isFavorite = articleBuilder.isFavorite;
        this.savedDate = articleBuilder.savedDate;
    }

    public static class Builder {
        private String url;
        private String title;
        private String path;
        private String tags;
        private String savedDate;
        boolean isFavorite;


        public Builder url(String url) {
            this.url = url;
            return this;
        }


        public Builder path(String path) {
            this.path = path;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder tags(String tags) {
            this.tags = tags;
            return this;
        }


        public Builder savedDate(String savedDate) {
            this.savedDate = savedDate;
            return this;
        }


        public Builder isFavorite(boolean isFavorite) {
            this.isFavorite = isFavorite;
            return this;
        }

        public ArticleModel build() {
            if (url == null)
                throw new IllegalStateException("url cannot be null");
            if (path == null)
                throw new IllegalStateException("path cannot be null");
            if (tags == null) {
                throw new IllegalStateException("tags cannot be null");
            }

            return new ArticleModel(this);
        }
    }

    @Override
    public String toString() {
        return String.format("url -> %s \n storageDir-> %s", url, path);
    }

    public String getUrl() {
        return url;
    }

    public String getPath() {
        return path;
    }

    public String getTags() {
        return tags;
    }

    public String getTitle() {
        return title;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public String getSavedDate() {
        return savedDate;
    }

}

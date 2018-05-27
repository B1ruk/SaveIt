package io.start.biruk.saveit.model.db;

import com.annimon.stream.Stream;
import com.google.common.base.CaseFormat;
import com.google.common.base.Converter;
import com.google.common.base.Objects;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created by biruk on 5/10/2018.
 */
@DatabaseTable(tableName = ArticleModel.ARTICLE_TABLE)
public class ArticleModel implements Serializable{

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

    private ArticleModel(ArticleBuilder articleBuilder) {
        this.url = articleBuilder.url;
        this.title = articleBuilder.title;
        this.path = articleBuilder.path;
        this.tags = articleBuilder.tags;
        this.isFavorite = articleBuilder.isFavorite;
        this.savedDate = articleBuilder.savedDate;
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

    public static class ArticleBuilder {
        private String url;
        private String title;
        private String path;
        private String tags;
        private String savedDate;
        boolean isFavorite;


        public ArticleBuilder url(String url) {
            this.url = url;
            return this;
        }


        public ArticleBuilder path(String path) {
            this.path = path;
            return this;
        }

        public ArticleBuilder title(String title) {
            this.title = title;
            return this;
        }

        public ArticleBuilder tags(String tags) {
            this.tags = tags;
            return this;
        }


        public ArticleBuilder savedDate(String savedDate) {
            this.savedDate = savedDate;
            return this;
        }


        public ArticleBuilder isFavorite(boolean isFavorite) {
            this.isFavorite = isFavorite;
            return this;
        }

        public ArticleModel build() {
            if (url == null)
                throw new IllegalStateException("url cannot be null");
            if (path == null)
                throw new IllegalStateException("path cannot be null");

            return new ArticleModel(this);
        }
    }

    @Override
    public String toString() {
        return String.format("url -> %s \n storageDir-> %s", url, path);
    }
}

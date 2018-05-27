package io.start.biruk.saveit.model.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

/**
 * Created by biruk on 5/13/2018.
 */
public class ArticleDbHelper extends OrmLiteSqliteOpenHelper {

    public static final String DB_NAME="article.db";
    public static final int DB_VERSION=1;

    private Dao<ArticleModel,String> articleDao;

    public ArticleDbHelper(Context context){
        super(context,DB_NAME,null,DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource,ArticleModel.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {

    }

    public Dao<ArticleModel,String> getArticleDao() throws SQLException {
        if (articleDao==null){
            synchronized (ArticleDbHelper.class){
                if (articleDao==null){
                    this.articleDao=getDao(ArticleModel.class);
                }
            }
        }
        return articleDao;
    }
}

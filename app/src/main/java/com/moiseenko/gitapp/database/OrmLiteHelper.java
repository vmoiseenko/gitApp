package com.moiseenko.gitapp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.moiseenko.gitapp.json.Repositories;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Viktar_Maiseyenka on 18.01.2016.
 */
public class OrmLiteHelper extends OrmLiteSqliteOpenHelper {

    private static final String TAG = OrmLiteHelper.class.getSimpleName();

    private static final String DATABASE_NAME = "github.db";
    private static final int DATABASE_VERSION = 1;

    private RepositoryDAO repositoryDAO = null;

    public OrmLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    private static OrmLiteHelper databaseHelper;

    public static OrmLiteHelper getHelper(){
        return databaseHelper;
    }
    public static void setHelper(Context context){
        databaseHelper = OpenHelperManager.getHelper(context, OrmLiteHelper.class);
    }
    public static void releaseHelper(){
        OpenHelperManager.releaseHelper();
        databaseHelper = null;
    }

    public void addRepository(Repositories.Repos repos) {
        try {
            getRepositoryDAO().createOrUpdate(repos);
            Log.d("add repository: ", repos.getFull_name());

        } catch (SQLException e) {
            Log.e("repos", repos.getFull_name()+ e.getStackTrace().toString(), e);
        }
    }

    public void addListOfRepositories(List<Repositories.Repos> list) {
        if(list != null){
            for (Repositories.Repos repos:list) {
                addRepository(repos);
            }
        }
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try
        {
            TableUtils.createTable(connectionSource, Repositories.Repos.class);
        }
        catch (SQLException e){
            Log.e(TAG, "error creating DB " + DATABASE_NAME);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {

    }

    @Override
    public void close() {
        super.close();
        repositoryDAO = null;
    }

    public RepositoryDAO getRepositoryDAO() throws SQLException {
        if(repositoryDAO == null){
            repositoryDAO = new RepositoryDAO(getConnectionSource(), Repositories.Repos.class);
        }
        return repositoryDAO;
    }

    public class RepositoryDAO extends BaseDaoImpl<Repositories.Repos, Integer> {

        protected RepositoryDAO(ConnectionSource connectionSource, Class<Repositories.Repos> dataClass) throws SQLException{
            super(connectionSource, dataClass);
        }

        public List<Repositories.Repos> getAllRoles() throws SQLException {
            return this.queryForAll();
        }
    }
}

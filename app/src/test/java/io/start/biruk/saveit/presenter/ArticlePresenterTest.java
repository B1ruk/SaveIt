package io.start.biruk.saveit.presenter;

import junit.framework.Assert;

import org.junit.Test;

import okhttp3.HttpUrl;

import static org.junit.Assert.*;

/**
 * Created by biruk on 5/17/2018.
 */
public class ArticlePresenterTest {

    @Test
    public void testHttpParse(){
        String mainUrl="http://www.xyz.com/";
        String subUrl="/abc";
        String actual = "http://www.xyz.com/abc";
        String expected="";

        if (mainUrl.endsWith("/") && subUrl.startsWith("/"))
            expected=mainUrl+subUrl.substring(1,subUrl.length());

        System.out.println(String.format("\turl ->%s\n\tparseurl->%s",actual,expected));
        Assert.assertEquals(expected,actual);
    }

}
package com.alan.databee;

import com.alan.databee.common.cache.ComLoader;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@SpringBootTest
public class CacheTest {

    private LoadingCache<String, String> classCache;

    @Test
    public void test() throws ExecutionException {
        CacheLoader<String, String> loader = new loader();
        classCache = CacheBuilder.newBuilder().maximumSize(64)
                .expireAfterAccess(4, TimeUnit.HOURS)
                .build(loader);
        try {
            String adfsdf = classCache.get("adfsdf");
        }catch (RuntimeException e){
            e.printStackTrace();
        }


    }
}
class loader extends CacheLoader<String,String>{
    @Override
    public String load(String s) throws Exception {
        return null;
    }
}


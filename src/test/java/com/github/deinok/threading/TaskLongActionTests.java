package com.github.deinok.threading;


import org.junit.Test;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.concurrent.Callable;

public class TaskLongActionTests {

    @Test
    public void internetTest() {
        final Task<String> stringTask = new Task<String>(new Callable<String>() {
            public String call() throws Exception {
                final URL url = new URL("http://example.com/");
                final InputStream inputStream = url.openConnection().getInputStream();  // throws an IOException
                final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                String result = "";
                String temporaryString;
                while ((temporaryString = bufferedReader.readLine()) != null) {
                    result += temporaryString + "\n";
                }
                return result;
            }
        }).executeAsync();

        final String result = stringTask.getResult();
        System.out.println(result);
    }
}

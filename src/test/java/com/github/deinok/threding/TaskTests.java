package com.github.deinok.threding;

import com.github.deinok.threading.Task;
import com.github.deinok.threading.TaskRunnable;
import org.junit.Test;

public class TaskTests {

      @Test(timeout = 10000)
      public void sleepTest(){
          Task<Void> task = new Task<Void>(new TaskRunnable<Void>() {
              @Override
              public Void execute() {
                  try {
                      Thread.sleep(5000);
                  } catch (InterruptedException e) {
                      throw new RuntimeException(e);
                  }
                  return null;
              }
          }).executeAsync();

          try {
              Thread.sleep(5000);
          } catch (InterruptedException e) {
              throw new RuntimeException(e);
          }
          task.await();
      }
}

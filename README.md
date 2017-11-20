# RxTaskDemo
异步任务(AsyncTask)改良版
### 导入
```text
compile 'com.hbin:rxtask:1.0.0'
```
### 当前线程
任务执行在当前线程上
```Java
RxTask.create()
      .scheduler(RxTask.THREAD_CURR)
      .post(new Runnable() {
      
             @Override
             public void run() {
                //执行任务
             }
      });
 ```
### UI主线程
任务执行在UI主线程上
```Java
RxTask.create()
      .scheduler(RxTask.THREAD_UI)
      .post(new Runnable() {
      
             @Override
             public void run() {
                //执行任务
             }
      });
 ```
 ### 工作线程
 任务执行在工作线程上
 ```Java
RxTask.create()
      .scheduler(RxTask.THREAD_WORK)
      .post(new Runnable() {
      
             @Override
             public void run() {
                //执行任务
             }
      });
 ```
 ### 工作线程
 任务执行在工作线程上, 并返回task
```Java
Task task = RxTask.create()
                  .workThread()
                  .bindLifeCycle(this)
                  .execute(new Runnable() {
      
                       @Override
                       public void run() {
                          //执行任务
                       }
                  });
  .......
  //取消任务
  task.cancelTask();
```
### 异步任务
执行异步任务
```Java
Task task = RxTask.<Void, Integer, Boolean>async()
              .bindLifeCycle(this)
              .doInBackground(new RxDoInBackground<Void, Integer, Boolean>() {

                     @Override
                     public Boolean doInBackground(ProgressTask<Integer> progressTask, Void... params) {
                         int n = 0;
                         .........
                         //后台执行任务
                         .........                    
                         //更新进度值
                         progressTask.updateProgress(++n);
                     }
               })
               .progressUpdate(new RxProgressUpdate<Integer>() {
               
                     @Override
                     public void onProgressUpdate(Integer... values) {
                         //进度更新
                     }
               })
               .postExecute(new RxPostExecute<Boolean>() {

                     @Override
                     public void onPostExecute(Boolean reuslt) {
                         //UI控件更新
                     }
               })
               .execute();
  .......
  //取消任务
  task.cancelTask();                
```      
## License 
```text
Copyright 2016 hbin2015

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

# arnylib
общая библиотека
Clone into project modules folder `git submodule add https://github.com/arnyigor/arnylib.git modules/arnylib`

In  **settings.gradle** 
`include ':app',':arnylib'`
`project(':arnylib').projectDir = new File(rootProject.projectDir, 'modules/arnylib')`

In **app/build.gradle**
 `compile project(path: ':arnylib')`

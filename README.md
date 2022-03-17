初始版本的小工具以工具类的方式实现，通过二方包提供给用户使用，主要带来以下几点不足：

- 使用成本高，需要单独以main方法的方式启动整个工具
- 依赖二方包，造成代码侵入
- 依赖项目的编译结果
- 需要单独copy出代码并粘贴在指定位置

so，基于这几个缺点和不足以集成开发软件插件的方式实现该功能是必要的
github: [https://github.com/Aumin/MapStructMappingTools](https://github.com/Aumin/MapStructMappingTools)

## 使用步骤

### 1.安装MapStructMappingTool Plugin

方式一：本地下载压缩包
[MapStructMappingTools.zip](https://hellobike.yuque.com/attachments/yuque/0/2022/zip/633967/1647511729551-7067be1e-5df9-42bf-9fa6-2440b85aa43f.zip?_lake_card=%7B%22src%22%3A%22https%3A%2F%2Fhellobike.yuque.com%2Fattachments%2Fyuque%2F0%2F2022%2Fzip%2F633967%2F1647511729551-7067be1e-5df9-42bf-9fa6-2440b85aa43f.zip%22%2C%22name%22%3A%22MapStructMappingTools.zip%22%2C%22size%22%3A433446%2C%22type%22%3A%22application%2Fzip%22%2C%22ext%22%3A%22zip%22%2C%22status%22%3A%22done%22%2C%22taskId%22%3A%22u52038862-ed22-4e88-be13-6be47ba9ec1%22%2C%22taskType%22%3A%22upload%22%2C%22id%22%3A%22uf892db77%22%2C%22card%22%3A%22file%22%7D)
Preferences | Plugins进入如下页面选择Install plugin from disk，选择下载好的压缩包
![image.png](https://cdn.nlark.com/yuque/0/2022/png/633967/1647511793728-291367e4-0875-4e5a-b93c-950bfe5f240b.png#clientId=ueb06a0ae-c503-4&crop=0&crop=0&crop=1&crop=1&from=paste&height=708&id=u8a22e5c9&margin=%5Bobject%20Object%5D&name=image.png&originHeight=1416&originWidth=1994&originalType=binary&ratio=1&rotation=0&showTitle=false&size=593062&status=done&style=none&taskId=uf70e13dc-6ccd-4750-9186-25400c2d861&title=&width=997)
重启Idea.
方式二：Preferences | Plugin | Marketplace搜索MapStructMappingTool，直接安装后重启即可
官网审批中

### 2.启动

选择需要MapperStruct映射类中的方法，鼠标右键菜单栏中选择MapStructMappingTool
![image.png](https://cdn.nlark.com/yuque/0/2022/png/633967/1647512576437-cccd2d42-1852-47da-ae30-4c97ae9fbc8c.png#clientId=ueb06a0ae-c503-4&crop=0&crop=0&crop=1&crop=1&from=paste&height=434&id=uf863c698&margin=%5Bobject%20Object%5D&name=image.png&originHeight=1452&originWidth=1674&originalType=binary&ratio=1&rotation=0&showTitle=false&size=859769&status=done&style=none&taskId=u351af228-8699-40c1-98ae-68d5a34be0b&title=&width=500)
注意📢：

- 方法签名需要选择完整，正确示范如下

![image.png](https://cdn.nlark.com/yuque/0/2022/png/633967/1647512726637-966128e8-4756-401d-b283-d26e20b5d912.png#clientId=ueb06a0ae-c503-4&crop=0&crop=0&crop=1&crop=1&from=paste&height=70&id=uc72b9442&margin=%5Bobject%20Object%5D&name=image.png&originHeight=140&originWidth=812&originalType=binary&ratio=1&rotation=0&showTitle=false&size=47266&status=done&style=none&taskId=u0dcd1f0f-f575-47da-9083-f45d1f56a94&title=&width=406)
或者直接双击整行
![image.png](https://cdn.nlark.com/yuque/0/2022/png/633967/1647512763021-fe8a3e66-6d82-46a8-90b3-b5db0548f00a.png#clientId=ueb06a0ae-c503-4&crop=0&crop=0&crop=1&crop=1&from=paste&height=95&id=u5605b7df&margin=%5Bobject%20Object%5D&name=image.png&originHeight=190&originWidth=1132&originalType=binary&ratio=1&rotation=0&showTitle=false&size=55397&status=done&style=none&taskId=ud48a6948-33a6-48e8-ba09-31328b8320f&title=&width=566)

- 只能在维护MapperStruct的映射类文件中使用

使用了类注解@Mapper

- 维护映射关系的方法只能是1-1类型，暂不支持1-N的映射关系，正确示范如下

GeoInfo convertGeoInfo(Address address)**;**
**或者 **void convertGeoInfo(@MappingTargetGeoInfo geoInfo,  Address address)**;**

### 3.定义映射规则

启动后展示源类文件的字段名和字段类型，左边主窗口展示的是源类文件的字段信息，右边为目标文件的字段信息。
![image.png](https://cdn.nlark.com/yuque/0/2022/png/633967/1647513460108-385006ba-d249-4e67-8085-4bad7e5e856e.png#clientId=ueb06a0ae-c503-4&crop=0&crop=0&crop=1&crop=1&from=paste&height=410&id=u423a0d41&margin=%5Bobject%20Object%5D&name=image.png&originHeight=1200&originWidth=2000&originalType=binary&ratio=1&rotation=0&showTitle=false&size=1093547&status=done&style=none&taskId=u7b191879-9eb3-436b-b962-ccc13289ff2&title=&width=684)
定义好映射关系后，点击上方output按钮将映射代码输出到指定方法位置。
![image.png](https://cdn.nlark.com/yuque/0/2022/png/633967/1647513716427-b6c3eb0f-21a4-48b2-9e83-90d5dd2b435f.png#clientId=ueb06a0ae-c503-4&crop=0&crop=0&crop=1&crop=1&from=paste&height=412&id=u57279fb9&margin=%5Bobject%20Object%5D&name=image.png&originHeight=1200&originWidth=2000&originalType=binary&ratio=1&rotation=0&showTitle=false&size=1213507&status=done&style=none&taskId=ub3441eec-60df-4364-bad4-1ad8dfbc761&title=&width=686)
![image.png](https://cdn.nlark.com/yuque/0/2022/png/633967/1647513758388-b177b2d2-b2b0-4eff-aece-d6733d438856.png#clientId=ueb06a0ae-c503-4&crop=0&crop=0&crop=1&crop=1&from=paste&height=168&id=ufd7b74e7&margin=%5Bobject%20Object%5D&name=image.png&originHeight=336&originWidth=1074&originalType=binary&ratio=1&rotation=0&showTitle=false&size=242588&status=done&style=none&taskId=ue9444065-0dc6-4f16-9420-667efd7da88&title=&width=537)

### 4.其他

- reset，重置映射关系
- revoke，撤销上次字段映射关系，删除一条映射规则
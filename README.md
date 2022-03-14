<h1> MapStructMappingTool IDEA Plugin 使用指南</h1>

这将帮助你快速且便利的获取到对象->对象的映射关系，并且生成MapStruct对应的字段映射代码
<br/>github：<a href="https://github.com/Aumin/MapStructMappingTools">https://github.com/Aumin/MapStructMappingTools</a>
<br/>help doc：详见README.md<br>
<ul>
<li>使用方式: 在标注了是MapperStruct维护的映射类中双击需要映射的方法然后鼠标右键选择弹出的MapStructMappingTool功能栏，即可弹出sourceObject和targetObject的类字段树，手动指明字段的映射关系，映射完毕后点击output按钮将自动生成的映射代码输出到对应的映射方法上方</li>
<li>使用场景: 需要维护MapStruct映射类时，需要手动编写字段映射关系，它或许能帮助你更便利或者更舒服的去做这些工作，
特别一些场景：
1.遇到字段含义相同但是字段命名不一样的情况我们需要在对应的类文件中来回跳转确认字段名，在回到映射维护类编写类似"@Mapping(source = "xxx", target = "xxx")的代码；
2.有很多字段需要你去编写映射关系，映射的目标属性和源属性的命名是一样，但是对应的字段层级所处位置较深，这时我们还要额外注意对应的层级关系是否正确
3.你的项目中可能强制不使用MapStruct的隐式装换能力
</li>
<li>使用注意事项: by detecting @ data annotation, integrate Lombok generation strategy, or create get and set methods</li>
</ul>
<br/>最后：如果遇到使用问题请联系本人，email="wutailong11062@hellobike.com"<br>
<br/>后续会给出该插件的构建过程<br>

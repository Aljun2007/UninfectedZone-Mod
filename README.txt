这是我的世界模组 UninfectedZone-1.18.2 的源码

计划测试beta版发布：8月底

模组更新迟缓原因：
    1.高中学业
    2.独立开发
    3.重写逻辑以求更好的流畅度
    4.重新平衡数值以获得更好游玩体验

目前已经完成的内容：
    1.基础框架基本完成搭建（注册表，各种接口）
    2.僵尸核心行为基本（细节待调整）完成（破坏方块，搭建方块等）
    3.配置json的逻辑完成，添加许多更灵活的配置（僵尸类型，僵尸装备，僵尸属性）（自定义你的末日），
    4.GUI框架基本完成，语言文件和逻辑待嵌入
    5.添加了对僵尸皮肤材质包 [Tissou's Zombie Pack] 的支持，实现免Optfine加载材质（可在Client配置中关闭），游戏稳定性提升
    6.对僵尸寻路机制的改进，以前旧版一百只僵尸就卡得不行，现在实测200只僵尸无高清修复都更流畅
    7.添加对其他模组僵尸的支持，如果不支持就在配置json中添加就支持了
    8.已支持的联动
        a. timeless-and-classics-zero : 僵尸会听到枪声，因此你需要消音器
        b. mrcrayfishs-gun-mod : 僵尸会听到枪声，因此你需要消音器
        c. villager-guard : 与旧版一致
    9.扩展了原版Minecraft数据包的condition种类（也可以用在自己的数据包），并融入了配置config中


计划优先度高的内容：
    1.与 末日生存工具包 Zombie Survival Kit 联动，以完成感染系统
    2.攻城（尸潮，尸群迁徙）
    3.安全区系统(核心功能已实现)
    4.游戏内的一些小提示，小提醒

计划优先度低的内容内容：
    1.废弃幸存者营地、废弃军事基地、废弃实验室等结构自然生成
    2.剧情
    3.更多游戏模式（研制解药模式）
    4.更多僵尸类型
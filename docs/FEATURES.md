- [目录](../README.md)
- [功能 (Features)](#功能-Features)
  * [AntiCreeper](#AntiCreeper)
  * [Back](#Back)
  * [BackDeath](#BackDeath)
  * [Disenchant](#Disenchant)
  * [Home](#Home)
  * [JoinInfo](#JoinInfo)
  * [PlayerTag](#PlayerTag)
  * [ScheduleNotice](#ScheduleNotice)
  * [SkipNight](#SkipNight)
  * [Suicide](#Suicide)
  * [Tpa](#Tpa)
  * [Warp](#Warp)
  * [WLKitsPlugin](#WLKitsPlugin)
  * [HttpApi](#HttpApi)

## 功能 (Features)

### AntiCreeper
防止 `Creeper` 爆炸破坏地形, 同时不影响 `mobGriefing=false` 引起的一系列问题

### Back
*[command: /back]*
*[permission: wlkits.cmd.back]*

允许玩家使用命令 `/back` 返回上一个地点

### BackDeath
*[command: /backdeath, /backd, /bd]*
*[permission: wlkits.cmd.backdeath]*

允许玩家使用命令 `/backdeath`(简写 `/backd` 或 `/bd`) 返回上一个死亡点

### Disenchant
*[permission: wlkits.action.disenchant]*

允许玩家将一个物品上的附魔转移到附魔书上

#### 用法

1. **合成转移附魔书**: 八个萤石粉, 中间一本书
2. **开始转移**: 把要转移的物品放在副手, 附魔书放在主手, 按右键即可

### Home
*[permission: wlkits.cmd.home]*

允许玩家设置家并随时回家

#### 用法

- **设置家**: `/sethome`
- **回家**: `/home`
- **删除家**: `/delhome`

### JoinInfo

当玩家进入或离开服务器时, 在聊天栏和控制台广播消息

### PlayerTag
*[command: /playertag, /ptag]*
*[permission: wlkits.cmd.playertag]*

允许玩家使用命令 `/playertag`(简写 `/ptag`) 设置称号 (即前缀)

### ScheduleNotice

循环在服务器中广播公告

### SkipNight
*[permission: wlkits.action.skipnight]*

使用睡觉玩家在整个玩家数量所占百分比计算跳过夜晚需要玩家的数量, 如

- `0%` -> 只要有玩家入睡即可跳过夜晚

- `50%` -> 有一半的玩家入睡即可跳过夜晚

- `100%` -> 所有玩家入睡即可跳过夜晚

### Suicide
*[permission: wlkits.cmd.suicide]*

允许玩家使用命令 `/suicide` 结束自己的生命

### Tpa
*[permission: wlkits.cmd.tpa]*

允许玩家使用命令 `/tpa` 向其他玩家发送传送请求

#### 用法

- **发送请求**: `/tpa [player]`
- **接受请求**: `/tpaccept` 或 `/tpac`
- **拒绝请求**: `/tpadeny` 或 `/tpad`
- **取消请求**: `/tpacancel`
- **查看帮助**: `/tpahelp`

### Warp
*[permission: wlkits.cmd.warp]*
*[permission: wlkits.cmd.warplist]*

允许玩家使用命令 `/warp` 传送到地标点

创建地标点时的 `private` 和 `public` 分别表示 `私有地标` 和 `共有地标`,

私有地标除了创建者, 其他玩家无法传送; 共有地标所有玩家都可以传送

#### 用法

- **传送**: `/warp [name]`
- **创建地标点**: `/setwarp [private/public] [name]`
- **删除地标点**: `/delwarp [name]`
- **查看帮助**: `/warphelp`
- **查看所有地标点** (仅拥有权限的玩家可以使用): `/warplist`

### WLKitsPlugin
*[permission: wlkits.cmd.wlkits]*

插件本体命令集合

#### 用法

- **查看帮助**: `/wlkits help`
- **重载插件**: `/wlkits reload`
- **查看子插件开启状态**: `/wlkits status [pluginName]`
- **查看插件信息**: `/wlkits info`

### HttpApi

管理服务器的 HttpApi

#### 用法

- `/api/player/info`

| API 路径            | 说明               |
| ------------------ | ------------------ |
| /api/player/info   | 查看玩家的各项信息    |

| 参数        | 类型   | 说明     | 可能的值 |
| ---------- | ------ | ------- | ------- |
| token      | String | Token   | *       |
| playerName | String | 玩家名称 | *       |

- `/api/player/manage`

| API 路径            | 说明    |
| ------------------ | ------- |
| /api/player/manage | 操作玩家 |


| 参数        | 类型   | 说明     | 可能的值 |
| ---------- | ------ | ------- | ------- |
| token      | String | Token   | *       |
| playerName | String | 玩家名称 | *       |
| action     | String | 操作类型 | kick, kill, ban, pardon, clear, tp, msg (见 [Action.kt](https://github.com/WindLeaf233/WLKits-Reforged/blob/master/src/main/java/ml/windleaf/wlkitsreforged/plugins/httpapi/handlers/player/Action.kt)) |

- `/api/server/info`

| API 路径          | 说明              |
| ---------------- | ----------------- |
| /api/server/info | 查看服务器的各项信息 |

| 参数   | 类型   | 说明   | 可能的值 |
| ----- | ------ | ----- | ------- |
| token | String | Token | *       |


- `/api/server/execute`

| API 路径             | 说明         |
| ------------------- | ------------ |
| /api/server/execute | 执行服务器命令 |

| 参数      | 类型   | 说明      | 可能的值 |
| -------- | ------ | -------- | ------- |
| token    | String | Token    | *       |
| command  | String | 执行的命令 | *       |
| executor | String | 执行者    | (空字符串 -> 控制台) 或 (* -> 玩家) |

# 启航的班级管理系统

一个前后端分离的班级与员工管理后台，包含 JWT 登录、部门/员工/班级/学员管理、头像上传和学员数据统计。

## 功能

- JWT 登录鉴权，除登录与静态图片外的接口均需 `Authorization: Bearer <token>`
- 部门管理：查询、新增、修改、删除
- 员工管理：条件分页、工作经历维护、头像上传
- 班级管理：条件分页、班主任关联、开班/在读/结课状态计算
- 学员管理：条件分页、增删改查、违纪次数与扣分处理
- 数据统计：班级人数、学员学历分布
- 统一响应结构 `Result` 与分页结构 `PageResult`

## 技术栈

| 模块 | 技术 |
| --- | --- |
| 后端 | Spring Boot 4.1.1、MyBatis、PageHelper、JWT (jjwt)、MySQL 8、JDK 21 |
| 前端 | Vue 3、Vite 3、Element Plus 2.4.4、Axios、Vue Router |
| 部署 | Nginx（生产环境反向代理 `/api` 到 8080） |

## 目录结构

```text
web_ai_project/
├─ start_web_managenment/       # Spring Boot 后端
│  ├─ sql/
│  │  ├─ schema.sql             # 建库建表
│  │  └─ init_user.sql          # 演示账号
│  └─ src/main/
│     ├─ java/...               # controller / service / mapper / pojo
│     └─ resources/             # application.yml、Mapper XML
├─ qihang-class-management/     # Vue 3 前端
│  └─ src/                      # api / router / views / utils
├─ _backup/                     # 本机历史备份，已被 .gitignore 排除
└─ README.md
```

## 快速开始

### 1. 环境准备

- JDK 21
- Maven 3.9+
- MySQL 8.0+
- Node.js 18+ 与 npm

### 2. 初始化数据库

在项目根目录执行：

```bash
mysql -uroot -p -e "source start_web_managenment/sql/schema.sql"
mysql -uroot -p -e "source start_web_managenment/sql/init_user.sql"
```

初始化脚本会创建本地演示账号 `admin`。首次使用前，建议运行 `PasswordEncoderTool` 并传入新密码，再执行控制台输出的 SQL 完成重置。

### 3. 配置环境变量

数据库密码、JWT 密钥和上传目录不写入仓库。PowerShell 示例：

```powershell
$env:DB_URL = "jdbc:mysql://localhost:3306/start?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai"
$env:DB_USERNAME = "root"
$env:DB_PASSWORD = "<你的数据库密码>"
$env:JWT_SECRET = "<你的JWT密钥-至少32字节>"
$env:FILE_UPLOAD_DIR = "./uploads"
```

变量说明：

| 变量 | 默认值 | 说明 |
| --- | --- | --- |
| `DB_URL` | `jdbc:mysql://localhost:3306/start...` | JDBC 连接地址 |
| `DB_USERNAME` | `root` | 数据库用户名 |
| `DB_PASSWORD` | 空 | 数据库密码 |
| `JWT_SECRET` | 无，必填 | JWT 签名密钥，至少 32 字节 |
| `FILE_UPLOAD_DIR` | `./uploads` | 上传图片存放目录 |

### 4. 启动后端

```bash
mvn -f start_web_managenment/pom.xml -DskipTests package
java -jar start_web_managenment/target/qihang-class-management-0.0.1-SNAPSHOT.jar
```

后端地址：`http://localhost:8080`。

> 如果 `mvn` 不在 PATH 中，可直接使用 IntelliJ IDEA 自带的 Maven 执行 package。

### 5. 启动前端

```bash
cd qihang-class-management
npm install
npm run dev
```

访问 `http://localhost:5173`。开发服务器会把 `/api` 代理到 `http://localhost:8080`。

### 6. 生产构建与 Nginx

```bash
cd qihang-class-management
npm run build
```

将 `dist/` 内文件复制到 Nginx 的 `html/` 目录。Nginx 需要把 `/api/*` 去掉前缀后转发到后端 8080，并把 `/images/*` 转发到后端。

## 接口示例

### 1. 登录：输入

```http
POST /login
Content-Type: application/json
```

```json
{
  "username": "admin",
  "password": "<你的登录密码>"
}
```

### 2. 登录：输出

```json
{
  "code": 1,
  "msg": "success",
  "data": {
    "token": "<jwt-token>",
    "userId": 1,
    "username": "admin"
  }
}
```

PowerShell 调用示例：

```powershell
$body = '{"username":"admin","password":"<你的登录密码>"}'
$login = Invoke-RestMethod -Method Post -Uri "http://localhost:8080/login" `
  -ContentType "application/json" -Body $body

$headers = @{ Authorization = "Bearer $($login.data.token)" }
```

### 3. 分页查询员工：输入

```powershell
Invoke-RestMethod -Uri "http://localhost:8080/emps?page=1&pageSize=10&name=张" `
  -Headers $headers
```

### 4. 分页查询员工：输出

```json
{
  "code": 1,
  "msg": "success",
  "data": {
    "total": 1,
    "rows": [
      {
        "id": 1,
        "username": "zhangsan",
        "name": "张三",
        "gender": 1,
        "job": 2,
        "deptId": 1,
        "deptName": "教研部",
        "entryDate": "2024-03-01"
      }
    ]
  }
}
```

### 5. 上传头像：输入/输出

```bash
curl -X POST http://localhost:8080/upload \
  -H "Authorization: Bearer <token>" \
  -F "file=@avatar.png"
```

```json
{
  "code": 1,
  "msg": "success",
  "data": "/images/2f1c9a7e4b5d4c6f8a0e1b2c3d4e5f60.png"
}
```

仅允许 `jpg` / `png`，单文件默认不超过 10 MB。

## 主要接口

| 方法 | 路径 | 说明 |
| --- | --- | --- |
| POST | `/login` | 登录，无需 token |
| GET | `/me` | 当前登录用户 |
| GET/POST/PUT/DELETE | `/depts` | 部门管理 |
| GET/POST/PUT/DELETE | `/emps` | 员工管理；`GET /emps` 支持分页 |
| GET/POST/PUT/DELETE | `/clazzs` | 班级管理；`GET /clazzs` 支持分页 |
| GET/POST/PUT/DELETE | `/students` | 学员管理；`GET /students` 支持分页 |
| PUT | `/students/violation/{id}/{score}` | 违纪次数 +1，扣分 +score |
| GET | `/report/studentCountData` | 班级人数统计 |
| GET | `/report/studentDegreeData` | 学员学历统计 |
| POST | `/upload` | 上传 jpg/png 图片 |
| GET | `/images/{filename}` | 访问已上传图片 |

统一返回约定：

- 成功：`{"code":1,"msg":"success","data":...}`
- 失败：`{"code":0,"msg":"错误提示","data":null}`
- 分页：`data = {"total": 总条数, "rows": [当前页数据]}`

## 从 GitHub 同步

```bash
git clone https://github.com/<你的用户名>/<仓库名>.git
cd <仓库名>
git pull
```

首次推送本地仓库：

```bash
git remote add origin https://github.com/<你的用户名>/<仓库名>.git
git branch -M main
git push -u origin main
```

## 安全说明

- 不要提交 `.env`、数据库密码、JWT 密钥、IDE 配置、日志和上传文件。
- `.gitignore` 已排除 `_backup/`、`target/`、`dist/`、`node_modules/`、`.idea/` 等本机内容。
- 演示账号与密码仅用于本地学习，部署到公网前必须修改。
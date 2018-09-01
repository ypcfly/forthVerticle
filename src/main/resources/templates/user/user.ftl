<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
<div>
    <#list context.list >
        <ul>
            <#items as user>
                <li>
                    姓名：<a>${user.userName}</a>
                    年龄：<a>${user.age}</a>
                    国家：<a>${user.address}</a>
                </li>
            </#items>
        </ul>
    </#list>
</div>
</body>
</html>

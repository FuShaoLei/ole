<#ftl encoding="UTF-8">
<#macro renderObjects nodeList>
    <#list nodeList as nodeItem>
        <#assign indentation = ""?right_pad(nodeItem.level,">")>
        <#if nodeItem.isArticle() >
            <div class="article-item">
                <a href="${nodeItem.url}" class="nav_link">${nodeItem.name}</a>
            </div>
        <#elseif nodeItem.isFolder()>
            <div class="folder-wrapper <#if nodeItem.level == 0 >folder-top-wrapper</#if>">
                <div class="folder-name">${indentation} ${nodeItem.name}</div>
                <#if nodeItem.fileNodeList??>
                    <@renderObjects nodeItem.fileNodeList/>
                </#if>
            </div>

        </#if>
    </#list>
</#macro>

<html lang="en">
<head>
    <meta charset="UTF-8">
    <style type="text/css">
        :root {
            --left-with: 300px;
            --color-bg: #1E1F22;
            --color-theme-main: #28ABAE;
            --color-text-main: #A9A9B3;
            --color-text-secondary: #333437;
            --color-embellish: #26282E;

            --font-main: 'Noto Serif SC', serif;
            --font-code: 'Roboto Mono', monospace;
        }

        body, html {
            margin: 0;
            padding: 0;
        }

        /* 滚动条 */
        ::-webkit-scrollbar {
            width: 8px;
            height: 8px;
        }

        ::-webkit-scrollbar-thumb {
            background: var(--color-text-secondary);
        }

        ::-webkit-scrollbar-thumb:hover {
            background: var(--color-text-secondary);
        }


        body {
            background: var(--color-text-secondary);
            color: var(--color-text-main);
            font-family: var(--font-main);
        }

        body a {
            color: var(--color-text-main);
            text-decoration: none;
        }

        body a:hover {
            color: var(--color-theme-main);
        }

        /* 总体框架 */
        body {
            display: flex;
            justify-content: center;
            align-content: center;
        }
        .main-wrapper {
            width: 1200px;
        }
        .leftArea {
            font-size: .9rem;
            position: fixed;
            top: 0;
            width: var(--left-with);
            height: 100%;
            overflow: auto;
            background: var(--color-embellish);
        }

        .rightArea {
            font-size: 1.1rem;
            margin-left: var(--left-with);
            padding: 30px;
            min-height: 100vh;
            background-color: var(--color-bg);
        }

        .article-wrapper img {
            max-width: 100%;
        }

        .folder-top-wrapper {
            margin-top: 12px;
        }

        .folder-name {
            font-weight: 900;
            font-size: 1rem;
            color: var(--color-theme-main);
            margin-bottom: 10px;
        }

        .article-item {
            margin: 0;
        }
        .article-item a {
            display: block;
            padding: 7px 8px;
        }
        .article-item a:hover{
            color: var(--color-text-main);
            background: var(--color-bg);
        }
        .nav_link.active_link {
            color: var(--color-text-main);
            background: var(--color-bg);
        }

        /* 文章样式 */
        pre {
            /*background: var(--color-embellish)!important;*/
            /*padding: 1em;*/
            overflow-x: auto;
        }

        code {
            background-color: var(--color-embellish);
            font-family: var(--font-code);
            font-size: 15px;
            padding: 0 5px;
        }

        pre code {
            border: none;
            background: none;
            font-family: var(--font-code);
            font-size: 15px;
        }

        pre code::before {
            content: none;
        }

        pre code::after {
            content: none;
        }

        blockquote {
            margin: 0;
        }

        blockquote p {
            display: contents;
        }

        blockquote::before {
            content: '> ';
            color: var(--color-theme-main);
            font-weight: 900;
        }

        .article-wrapper a {
            color: var(--color-theme-main);
        }
        .article-wrapper a::after{
            content: '↗';
        }
        .article-wrapper a:hover{
            text-decoration: underline;
        }

        .article-wrapper h1::after{
            content: '#';
            color: var(--color-theme-main);
            margin-left: 7px;
            font-weight: 900;
        }

        .article-wrapper h2::after{
            content: '##';
            color: var(--color-theme-main);
            margin-left: 7px;
            font-weight: 900;
        }

        .article-wrapper h3::after{
            content: '###';
            color: var(--color-theme-main);
            margin-left: 7px;
            font-weight: 900;
        }

    </style>
    <title>${title}</title>
</head>

<link rel="preconnect" href="https://fonts.googleapis.com">
<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
<link href="https://fonts.googleapis.com/css2?family=Noto+Serif+SC:wght@200;300;400;700;900&family=Roboto+Mono&display=swap"
      rel="stylesheet">
<link rel="stylesheet" href="https://cdn.jsdelivr.net/gh/highlightjs/cdn-release@11.9.0/build/styles/vs2015.css">
<script src="https://cdn.jsdelivr.net/gh/highlightjs/cdn-release@11.9.0/build/highlight.min.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">

<body>
<main class="main-wrapper">
    <aside class="leftArea">
        <div style="padding: 30px">
            <@renderObjects nodeList/>
        </div>
    </aside>

    <article class="rightArea flex-center">
        <div class="article-wrapper">
            ${article}
        </div>
    </article>
</main>
<script>
    hljs.highlightAll();

    let links = document.querySelectorAll(".nav_link");

    function isActive(pathname) {
        let rootPath = decodeURIComponent(window.location.pathname)
        let path = pathname + "/"

        console.log("isActive:rootPath = "+rootPath)
        console.log("isActive:pathname = "+pathname)
        console.log("isActive:path = "+path)

        // const decodedString = decodeURIComponent(encodedString);


        return pathname === rootPath || path === rootPath;
    }

    for (let link of links) {
        let linkPath = link.getAttribute("href");

        console.log("linkPath => "+linkPath);

        if (isActive(linkPath)) {
            console.log("active linkPath => "+linkPath)
            link.className = "nav_link active_link";
        }
    }
</script>
</body>
</html>


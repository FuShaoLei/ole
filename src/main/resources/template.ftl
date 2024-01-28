<#macro renderObjects nodeList>
    <#list nodeList as nodeItem>
        <#assign indentation = ""?right_pad(nodeItem.level,">")>
        <#if nodeItem.isArticle() >
            <div class="article-item">
                ${indentation} <a href="${nodeItem.url}">${nodeItem.name}</a>
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
            --color-theme-main: antiquewhite;
            --color-text-main: #A9A9B3;
            --color-text-secondary: #333437;
            --color-embellish: #26282E;

            --font-main: 'Noto Serif SC', serif;
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
            background-color: var(--color-bg);
            color: var(--color-text-main);
            font-family: var(--font-main);
        }

        body a {
            color: var(--color-text-main);
            text-decoration: none;
        }
        body a:hover{
            color: var(--color-theme-main);
        }

        .leftArea {
            font-size: .9rem;
            position: fixed;
            top: 0;
            left: 0;
            width: var(--left-with);
            height: 100%;
            overflow: auto;
            background: var(--color-embellish);
        }

        .rightArea {
            font-size: 1.1rem;
            margin-left: var(--left-with);
            padding: 30px;
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
            color: antiquewhite;
        }
        .article-item{
            margin: 2px 0;
        }
    </style>
    <title>${title}</title>
</head>

<link rel="preconnect" href="https://fonts.googleapis.com">
<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
<link href="https://fonts.googleapis.com/css2?family=Noto+Serif+SC:wght@200;300;400;700;900&family=Roboto+Mono&display=swap"
      rel="stylesheet">

<body>
<main>
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
</body>
</html>


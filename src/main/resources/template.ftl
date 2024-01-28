<#macro renderObjects nodeList>
    <#list nodeList as nodeItem>
        <#assign indentation = "">
        <#if nodeItem.level == 1>
            <#assign indentation = "-">
        <#elseif nodeItem.level == 2>
            <#assign indentation = "--">
        </#if>
        <#if nodeItem.isArticle() >
            <div>
                <a href="${nodeItem.url}">${indentation} ${nodeItem.name}</a>
            </div>
        <#elseif nodeItem.isFolder()>
            <div>${indentation} ${nodeItem.name}</div>
            <#if nodeItem.fileNodeList??>
                <@renderObjects nodeItem.fileNodeList/>
            </#if>
        </#if>
    </#list>
</#macro>

<html lang="en">
<head>
    <meta charset="UTF-8">
    <style type="text/css">
        article,
        footer,
        header,
        menu,
        nav,
        section {
            display: block;
        }

        body {
            font-size: .9rem;
            background-color: #2E3033;
            color: #DEDEDE;
        }

        body a {
            color: #DEDEDE;
        }

        .leftArea {
            bottom: 0px;
            left: 0px;
            overflow: auto;
            padding-left: 0.5em;
            padding-right: 0.5em;
            position: absolute;
            right: auto;
            text-align: left;
            top: 0em;
            width: 300px;
        }

        .rightArea {
            bottom: 0px;
            left: 350px;
            overflow: auto;
            padding-left: 0.5em;
            padding-right: 0.5em;
            position: absolute;
            right: 0px;
            text-align: left;
            top: 0em;
        }
    </style>
    <title>${title}</title>
</head>

    <body>
    <main>
        <aside class="leftArea">
            <@renderObjects nodeList/>
        </aside>

        <article class="rightArea">
            <div class="article-wrapper">
                ${article}
            </div>
        </article>
    </main>
    </body>
</html>


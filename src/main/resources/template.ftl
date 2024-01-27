<!DOCTYPE html>

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
            font-size: 1.4rem;
            background-color: #2E3033;
            color: #DEDEDE;
        }

        body a{
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
            left: 300px;
            overflow: auto;
            padding-left: 0.5em;
            padding-right: 0.5em;
            position: absolute;
            right: 0px;
            text-align: left;
            top: 0em;
            max-
        }
    </style>
    <title>${title}</title>
</head>

<body>
<main>
    <aside class="leftArea">
<#--        <#list nodeList as nodeItem>-->
<#--            <#if nodeItem.isArticle()>-->
<#--                <a href="${nodeItem.url}">${nodeItem.name}</a>-->
<#--            <#elseif nodeItem.isFolder()>-->
<#--                <div class="menu-item">-->
<#--                    <div class="menu-title">./${nodeItem.name}</div>-->
<#--                    <ul>-->
<#--                        <#list nodeItem.fileNodeList as childrenItem>-->
<#--                            <li><a href="${childrenItem.url}">${childrenItem.name}</a></li>-->
<#--                        </#list>-->
<#--                    </ul>-->
<#--                </div>-->
<#--            </#if>-->
<#--        </#list>-->
    </aside>

    <article class="rightArea">
        ${article}
    </article>
</main>
</body>
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
            font-size: .9rem;
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
        <#list nodeList as nodeItem>
            <#if nodeItem.isArticle() >
                <a href="${nodeItem.url}">${nodeItem.name}</a>
            <#else>
                欸嘿嘿 folder
            </#if>
        </#list>
    </aside>

    <article class="rightArea">
        ${article}
    </article>
</main>
</body>
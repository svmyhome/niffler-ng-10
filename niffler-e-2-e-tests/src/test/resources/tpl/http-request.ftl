<#ftl output_format="HTML">
<#-- @ftlvariable name="data" type="io.qameta.allure.attachment.http.HttpRequestAttachment" -->
<head>
  <meta http-equiv="content-type" content="text/html; charset = UTF-8">
  <link type="text/css"
        href="https://cdnjs.cloudflare.com/ajax/libs/highlight.js/11.7.0/styles/github.min.css"
        rel="stylesheet"/>
  <script type="text/javascript"
          src="https://cdnjs.cloudflare.com/ajax/libs/highlight.js/11.7.0/highlight.min.js"></script>
  <script type="text/javascript"
          src="https://cdnjs.cloudflare.com/ajax/libs/highlight.js/11.7.0/languages/json.min.js"></script>
  <script type="text/javascript">hljs.initHighlightingOnLoad();</script>
  <style>
    pre {
      white-space: pre-wrap;
    }
  </style>
</head>
<body>
<h4>HTTP Request</h4>
<div>
  <pre><code class="language-json">${data.method!"GET"}</code></pre>
  <pre><code class="language-json">${data.url!"Unknown"}</code></pre>
  <pre><code><#if data.body??>${data.body}</#if></code></pre>
</div>
</body>
</html>
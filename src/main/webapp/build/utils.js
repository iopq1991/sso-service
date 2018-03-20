var path = require('path'), fs = require('fs');
var config = require('../config')
var ExtractTextPlugin = require('extract-text-webpack-plugin')
var HtmlWebpackPlugin = require('html-webpack-plugin')
var appConfig = require('../config/app-config')

exports.assetsPath = function (_path) {
    var assetsSubDirectory = process.env.NODE_ENV === 'production'
        ? config.build.assetsSubDirectory
        : config.dev.assetsSubDirectory
    return path.posix.join(assetsSubDirectory, _path)
}

exports.cssLoaders = function (options) {
    options = options || {}

    var cssLoader = {
        loader: 'css-loader',
        options: {
            minimize: process.env.NODE_ENV === 'production',
            sourceMap: options.sourceMap
        }
    }

    // generate loader string to be used with extract text plugin
    function generateLoaders(loader, loaderOptions) {
        var loaders = [cssLoader]
        if (loader) {
            loaders.push({
                loader: loader + '-loader',
                options: Object.assign({}, loaderOptions, {
                    sourceMap: options.sourceMap
                })
            })
        }

        // Extract CSS when that option is specified
        // (which is the case during production build)
        if (options.extract) {
            return ExtractTextPlugin.extract({
                use: loaders,
                fallback: 'vue-style-loader'
            })
        } else {
            return ['vue-style-loader'].concat(loaders)
        }
    }

    // https://vue-loader.vuejs.org/en/configurations/extract-css.html
    return {
        css: generateLoaders(),
        postcss: generateLoaders(),
        less: generateLoaders('less'),
        sass: generateLoaders('sass', {indentedSyntax: true}),
        scss: generateLoaders('sass'),
        stylus: generateLoaders('stylus'),
        styl: generateLoaders('stylus')
    }
}

// Generate loaders for standalone style files (outside of .vue)
exports.styleLoaders = function (options) {
    var output = []
    var loaders = exports.cssLoaders(options)
    for (var extension in loaders) {
        var loader = loaders[extension]
        output.push({
            test: new RegExp('\\.' + extension + '$'),
            use: loader
        })
    }
    return output
}

//遍历pages文件夹生成入口
const pagesPath = './src/pages';
exports.getEntryPages = function () {
    var r = {}; // {polyfill: 'babel-polyfill'};
    getJsEntries().forEach(jsf => {
        var baseName = jsf.slice(0, -3);
        r[baseName] = pagesPath + '/' + jsf
    })

    return r;
}

exports.htmlPlugins = function () {
    var exChunks = process.env.NODE_ENV === 'production' ? ['manifest', 'vendor'] : [];
    var list = getJsEntries().map(jsf => {
        var baseName = jsf.slice(0, -3);


        return htmlPlugin({
            filename: baseName + '.html',
            chunks: [...exChunks, baseName],
            title: baseName
        })
    })
    //console.log(JSON.stringify([main, ...list], 0, 4))
    return list
}

function getJsEntries() {
    return fs.readdirSync(pagesPath).filter(f => f.slice(-3) === '.js')
}

function htmlPlugin(exConfig) {
    // generate dist index.html with correct asset hash for caching.
    // you can customize output by editing /index.html
    // see https://github.com/ampedandwired/html-webpack-plugin

    return new HtmlWebpackPlugin(Object.assign({
        template: 'html.tpl.html',
        inject: true,
        // minify: {
        //     removeComments: true,
        //     collapseWhitespace: true,
        //     removeAttributeQuotes: false
        //     // more options:
        //     // https://github.com/kangax/html-minifier#options-quick-reference
        // },
        title: '企业信息服务平台',
        // necessary to consistently work with multiple chunks via CommonsChunkPlugin
        // chunksSortMode: 'dependency',
        appConfig: appConfig
    }, exConfig))
}

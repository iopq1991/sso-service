var makeTheme = require('./make-element-theme.js');
var fs = require('fs'), path = require('path');
var HtmlWebpackPlugin = require('html-webpack-plugin')

module.exports = function (webpackConfig, next) {
    //生成element的theme
    //node build/build-argv.js -theme
    if (process.argv.indexOf('-theme') > -1) {
        makeTheme(next);
        return true;
    }
    else {
        var outIndex = process.argv.indexOf('-out');
        if (outIndex > -1) {
            webpackConfig.output.path = path.resolve(process.argv[outIndex + 1]);
        }

        //生成指定的entry
        //node build/build-argv.js -entry file1 file2 ...
        var entryPos = process.argv.indexOf('-entry');
        if (entryPos > -1) {
            var files = process.argv
                .slice(entryPos + 1)
                .map(fn => path.resolve(fn))
                .filter(fn => fs.existsSync(fn));
            webpackConfig.entry = files;
            //webpackConfig.plugins = webpackConfig.plugins.filter(checkHtmlPlugin)
            console.log('\nEntry: ' + files);
            next();
            return true;
        }
    }
}

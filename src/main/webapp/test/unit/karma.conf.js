// This is a karma config file. For more details see
//   http://karma-runner.github.io/0.13/config/configuration-file.html
// we are also using it with karma-webpack
//   https://github.com/webpack/karma-webpack

//debug command:
//node-debug node_modules\karma\bin\karma start test/unit/karma.conf.js --single-run
/*
纠正karma-coverage报错：
修改文件.\node_modules\karma-coverage\lib\reporter.js

var options = helper.merge({
          sourceStore: sourceStore
改为：
var options = helper.merge({
          sourceStore: null

 */
var path = require('path');
var webpackConfig = require('../../build/webpack.test.conf')
webpackConfig.module.rules.push({
    enforce: 'post',
    test: /\.js$/,
    loader: 'sourcemap-istanbul-instrumenter-loader',
    include: path.resolve('src'),
    exclude: [
        /\.(e2e|spec)\.ts$/,
        /node_modules/
    ]
});

module.exports = function (config) {
    config.set({
        // to run in additional browsers:
        // 1. install corresponding karma launcher
        //    http://karma-runner.github.io/0.13/config/browsers.html
        // 2. add it to the `browsers` array below.
        //browsers: ['PhantomJS'],
        //basePath: '.',
        browsers: ['MyChrome'],
        customLaunchers: {
            MyChrome: {
                base: 'Chrome',
                flags: ['--no-sandbox --disable-web-security --disable-gpu --headless']
            }
        },
        frameworks: ['mocha', 'sinon-chai'],//, 'phantomjs-shim'
        //reporters: ['spec', 'coverage', 'progress'],
        files: [
            // each file acts as entry point for the webpack configuration
            {pattern: './test-entry.js', watched: false},
            //{pattern: 'test/*_test.js', watched: false},
        ],
        preprocessors: {
            './test-entry.js': ['webpack', 'sourcemap', 'coverage']
        },
        webpack: webpackConfig,
        webpackMiddleware: {
            noInfo: false
        },
        coverageReporter: {
            dir: './coverage',
            reporters: [
                //{type: 'html', subdir: 'report-html'},
                {type: 'lcov', subdir: '.'},
                {type: 'text-summary'}
            ]
        },
        plugins: [
            'karma-*'
        ],
        client: {
            captureConsole: false,
        },

        port: 9876,
        colors: true,
        logLevel: config.LOG_INFO,
        autoWatch: true,
        singleRun: false,
        concurrency: Infinity,
        crossOriginAttribute: true, //生成Access-Control-Allow-Origin

        remapIstanbulReporter: {
            remapOptions: {}, //additional remap options
            reports: {
                'text-summary': null, // to display summary results on console
                json: 'coverage/coverage.json',
                lcovonly: 'coverage/lcov.info',
                html: 'coverage/html/',
            }
        },
        reporters: ['spec', 'coverage', 'progress', 'karma-remap-istanbul'],
    })
}

var builder = require('element-theme');

function run (cb) {
	// build
	builder.run({
		config: './src/assets/css/element-theme/theme-element-variables.scss',
		out: './src/assets/css/element-theme/dist',
		minimize: true
	}, cb);
}

//直接通过node调用
if (process.argv[1] && process.argv[1].match(/theme\.js$/))
	run();

module.exports = run;

const webpack = require('webpack');
const config = require('./webpack.config');

config.devtool = 'eval';

config.output.publicPath = '/static/jmetrics/';

config.output.pathinfo = true;

Object.keys(config.entry).forEach(key => {
  config.entry[key].unshift(require.resolve('react-dev-utils/webpackHotDevClient'));
});

config.plugins = [new webpack.HotModuleReplacementPlugin()];

module.exports = config;
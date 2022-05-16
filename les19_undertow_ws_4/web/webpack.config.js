const HtmlWebPackPlugin = require('html-webpack-plugin');
const path = require('path');
module.exports={
    optimization: {
        minimize: false
    },
	output: {
    filename: 'bundle.js',
     path: path.resolve(__dirname, '../src/main/resources'),
   },
    module: {     
        rules:[
            {
                test: /\.css$/i,
                use: ['style-loader', 'css-loader'],
              },
              {
                test: /\.(png|svg|jpg|jpeg|gif)$/i,
                use: 'file-loader',
              },
        ]
    },
    plugins: [
        new HtmlWebPackPlugin({
            template: "./src/index.html"
        })
    ]
}
const path = require("path");
const autoprefixer = require("autoprefixer");

module.exports = {
  entry: {
    // Using React:
	jmetrics_page: ["./src/main/js/jmetrics_page/index.js"]
  },
  output: {
    // The entry point files MUST be shipped inside the final JAR's static/
    // directory.
    path: path.join(__dirname, "../../target/classes/static"),
    filename: "[name].js"
  },
  resolve: {
    root: path.join(__dirname, "src/main/js")
  },
  externals: {
    // React 16.8 ships with SonarQube, and should be re-used to avoid 
    // collisions at runtime.
    react: "React",
    "react-dom": "ReactDOM",
    "sonar-request": "SonarRequest",
    "sonar-measures": "SonarMeasures",
    "sonar-components": "SonarComponents"
  },
  module: {
    loaders: [
      {
        test: /\.js$/,
        loader: "babel",
        exclude: /(node_modules)/
      },
      {
        test: /\.css/,
        loader: "style-loader!css-loader!postcss-loader"
      },
      { test: /\.json$/, loader: "json" }
    ]
  },
  postcss() {
    return [
      autoprefixer({
        browsers: [
          "last 3 Chrome versions",
          "last 3 Firefox versions",
          "last 3 Safari versions",
          "last 3 Edge versions",
          "IE 11"
        ]
      })
    ];
  }
};

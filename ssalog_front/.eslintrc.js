module.exports = {
  mode: "production",
  root: true,
  env: {
    node: true
  },
  extends: ["plugin:vue/essential", "@vue/airbnb", "plugin:prettier/recommended"],
  parserOptions: {
    parser: "babel-eslint"
  },
  rules: {
    "import/no-unresolved": "off",
    "no-console": process.env.NODE_ENV === "production" ? "warn" : "off",
    "no-debugger": process.env.NODE_ENV === "production" ? "warn" : "off",
    "prettier/prettier": [
      "error",
      {
        endOfLine: "auto"
      }
    ]
  }
};

var gulp = require('gulp');
var cleanCSS = require('gulp-clean-css');
var uglify = require('gulp-uglify');
var del = require('del');
var rename = require('gulp-rename');
var eslint = require('gulp-eslint');
var sass = require('gulp-sass')

var assetsSource = '';
var buildDestination = 'build/' + assetsSource;
var cssSource = assetsSource + 'css';
var cssDestination = buildDestination + 'css';
var jsSource = assetsSource + 'js';
var jsDestination = buildDestination + 'js';

//delete the build folders
gulp.task('delete' , function(){
  del([buildDestination], function(err){
    console.log('Build folder deleted');
  });
} );

//Minify the css
gulp.task('minify', function() {
  return gulp.src(cssSource + '/*.css')
    .pipe(cleanCSS())
    .pipe(rename({suffix: '.min'}))
    .pipe(gulp.dest(cssDestination));
});

//Compress the js
gulp.task('uglify', function() {
  return gulp.src( jsSource + '/*.js')
    .pipe(uglify())
    .pipe(rename({suffix: '.min'}))
    .pipe(gulp.dest(jsDestination));
});

gulp.task('sass', function () {
  return gulp.src(cssSource + '/*.scss')
    .pipe(sass().on('error', sass.logError))
    .pipe(gulp.dest(cssDestination));
});

//Lint the js
gulp.task('lint', function(){
  return gulp.src(jsSource + '/*.js')
    .pipe(eslint(
      {
        "rules": {
          "indent": [2, 2],
          "quotes": [2, "single"],
          "linebreak-style": [2, "unix"],
          "semi": [2, "always"]
        },
        "env": {
          "browser": true,
          "jquery": true
        },
        "extends": "eslint:recommended"
      }))
      .pipe(
        eslint.formatEach('compact', process.stderr)
      );
});

//Watch the folder for any changes
gulp.task('watch', function(){
  gulp.watch(cssSource, ['sass','minify']);
  gulp.watch(jsSource, ['uglify']);
});

gulp.task('sass:watch', function () {
  gulp.watch('./sass/**/*.scss', ['sass']);
});

//Setup the default task if "Gulp" is just run without a specific task
gulp.task('default', ['delete','sass','minify', 'uglify', 'watch']);

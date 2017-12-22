DOTDIR=~/.dot_files
commitMessage=$1
cd $DOTDIR;git add .;git commit -am "$commitMessage";cd -;

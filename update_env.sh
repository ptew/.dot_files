mkdir ~/.old_dot_files
files=(.aliases .bashrc .git-completion.bash .tmux.conf .vimrc)

for i in ${files[@]}; do
  echo "Moving: " $i
  mv ~/$i ~/.old_dot_files
  echo "Copying: " $i
  cp  ~/.dot_files/$i ~
done

source ~/.bashrc

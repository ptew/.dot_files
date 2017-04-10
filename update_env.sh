mkdir ~/.old_dot_files
files=(.aliases .bashrc .git-completion.bash .i3 .tmux.conf .vimrc)

for i in $(files); do
  echo "Moving: " $i
  mv ~/$i .old_dot_files
  cp -r ~/.dot_files/$i ~
done

source ~/.bashrc

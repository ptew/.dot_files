# load in the files variable from the update script
source ~/.dot_files/update_env.sh

for i in $(files); do
  echo "Updating: " $i
  cp -r ~/$i ~/.dot_files
done

cd ~/.dot_files
git commit -am "updating dot files..."
git push
cd -

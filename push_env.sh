# load in the files variable
source ~/.dot_files/files.sh

for i in ${files[@]}; do
  echo "Updating: " $i
done

cd ~/.dot_files
git commit -am "updating dot files..."
git push
cd -

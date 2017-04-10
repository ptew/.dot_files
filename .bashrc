source ~/.aliases

if [ -f ~/.git-completion.bash ]; then
	  . ~/.git-completion.bash
fi

#return modifified name or return nothing if no branch
function git-branch-prompt {
	  local branch=`git-branch-name`
	    if [ $branch ]; then printf ":%s " $branch; fi
}

#rename tab (tabname "lama")
function tabname {
	  printf "\e]1;$1\a"
}

#rename window (winname "bobby")
function winname {
	  printf "\e]2;$1\a"
}

# Git branch details
function parse_git_dirty() {
	        [[ $(git status 2> /dev/null | tail -n1) != *"working directory clean"* ]] && echo "*"
}

function parse_git_branch() {
	        git branch --no-color 2> /dev/null | sed -e '/^[^*]/d' -e "s/* \(.*\)/\1$(parse_git_dirty)/"
}

#one size fits all extract
extract () {
	   if [ -f $1 ] ; then
	   case $1 in
	   *.tar.bz2)   tar xvjf "$@";;
	   *.tar.gz)    tar xvzf "$@";;
	   *.bz2)       bunzip2 "$@";;
	   *.rar)       unrar "$@";;
	   *.gz)        gunzip "$@";;
	   *.tar)       tar xvf "$@";;
	   *.tbz2)      tar xvjf "$@";;
	   *.tgz)       tar xvzf "$@";;
	   *.zip)       unzip "$@";;
	   *.Z)         uncompress "$@";;
	   *.7z)        7z x "$@";;
	   *)           echo "don't know how to extract '$1'..." ;;
	   esac
	   else
	   echo "'$1' is not a valid file!"
	   fi
}

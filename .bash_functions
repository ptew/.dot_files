if [ -f ~/.git-completion.bash ]; then
	  . ~/.git-completion.bash
fi

#return modifified name or return nothing if no branch
function git-branch-prompt {
	  local branch=`git-branch-name`
	    if [ $branch ]; then printf ":%s " $branch; fi
}
#source http://thelucid.com/2012/01/04/naming-your-terminal-tabs-in-osx-lion/
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

alias la='ls -a'
alias ll='ls -l'
alias lla='ls -al'
alias l='ls -ltr'

#the update command you want
alias gs='git status'
alias gd='git diff'
alias gp='git pull '
alias gg='git grep '
alias ga='git add .'

#opens vim in folder view in the current folder
alias v='vim .'

#a lighter version of vim
alias vi='vim --noplugin'

#moving around alias
alias ..='cd ..'
alias ...="cd ../.."
alias ....="cd ../../.."
alias .....="cd ../../../.."
alias dropbox="cd /Users/ParkerTew/Dropbox"
alias classes="cd /Users/ParkerTew/Dropbox/classes"
alias docs="cd /Users/ParkerTew/Dropbox/Documents"

#.p
alias p='cat ~/.p'

#Mac to sleep
alias sleepy='pmset sleepnow'

#Do not disturb
alias dndoff='open '$HOME'/Library/DoNotDisturb/Disable\ Do\ Not\ Disturb.app'
alias dnd='open '$HOME'/Library/DoNotDisturb/Enable\ Do\ Not\ Disturb.app'

alias dickbutt='cat '$HOME'/.dickbutt'

alias mlb=HOME'/.mlb'

# Class alias
alias arc='ssh vlsifarm-03.mit.edu -l ptew'
alias lanka='ssh lanka.csail.mit.edu -l ptew'

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

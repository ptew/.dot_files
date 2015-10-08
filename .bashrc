export MARKPATH=$HOME/.marks
function jump { 
    cd -P $MARKPATH/$1 2>/dev/null || echo "No such mark: $1"
}
function mark { 
    mkdir -p $MARKPATH; ln -s $(pwd) $MARKPATH/$1
}
function unmark { 
    rm -i $MARKPATH/$1 
}
function marks {
    ls -l $MARKPATH | sed 's/  / /g' | cut -d' ' -f9- | sed 's/ -/\t-/g' && echo
}

export PATH=$PATH:/usr/local/mysql/bin
export PATH="/Applications/Postgres.app/Contents/Versions/9.4/bin:$PATH"
export LLVM_CONFIG="/opt/local/bin/llvm-config-mp-3.3"

export VIMRUNTIME=/usr/share/vim/vim73

#ignore hisotry repeats
export HISTCONTROL=ignoredups:ignorespace

#append to bash history if termianl quits
shopt -s histappend

#allows color in the terminal
export CLICOLOR=1

#import my file with all bash functions
source ~/.bash_functions

#Mac ls colors
export LSCOLORS=GxFxCxDxBxegedabagaced

#adds mark jump command to terminal
source ~/.marksrc

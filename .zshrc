# Lines configured by zsh-newuser-install
HISTFILE=~/.histfile
HISTSIZE=1000
SAVEHIST=1000
bindkey -v
# End of lines configured by zsh-newuser-install
# The following lines were added by compinstall
zstyle :compinstall filename '/home/pii/.zshrc'

autoload -Uz compinit
compinit
# End of lines added by compinstall

source ~/.aliases

# start tmux automatically
if [[ -z "$TMUX" ]]
then
  ID="`tmux ls | grep -vm1 attached | cut -d: -f1`"
  if [[ -z "$ID" ]]
  then
    tmux new-session
  else
    tmux attach-session -t "$ID"
  fi
fi

# Enable search of history
bindkey '^R' history-incremental-search-backward

export PS1="%m:%~ %% "

bindkey "^D" backward-word
bindkey "^C" forward-word
bindkey "^a" beginning-of-line
bindkey "^e" end-of-line

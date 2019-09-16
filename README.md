# Ansible-architecture-example
a small ansible architecture example to automate tasks (and some best practices)

## On how does it consist
this repo is shipped with a jenkinsfile (in the jenkins folder) which deploys ssh_keys in the destination machines.
some part are missing which are the jenkins config, the docker image for ansible 2.6.0 which must be declared in the jenkins conf as a node

## what's important
My opinion is to split the repo to 3 parts:

1- inventory

2- roles

3- playbooks

create git repo for ansible and link the above 3 repos as sub-modules

## Launch a playbook
to launch a playbook you have only to navigate to the root of the main repo and execute a playbook as it is shown below for the ssh role

```
ansible-playbook playbooks/infra/ssh.yml -i inventories/all/all/hosts --private-key /home/jenkins/workspace/RIC_CONFIG_ssh_tests_master-4GG2S22GQ3XURKVTVXZ7JSRC4FQJVDQRG5Q6J5IKE76XJOQYBQKA/ssh5067439706136108276.key -u jenkins -e target=all --check
```

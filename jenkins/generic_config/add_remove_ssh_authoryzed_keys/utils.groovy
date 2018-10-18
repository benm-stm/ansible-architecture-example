def stringToArrayParser(data, indexContentDelimiter, cellsDelimiter) {
    def map = [:]
    if (data) {
        data.split(cellsDelimiter).each {param ->
            def nameAndValue = param.split(indexContentDelimiter)
            map[nameAndValue[0]] = nameAndValue[1]
        }
    }
    return map
}

def tokenyze_input(input, delimiter=',') {
    list = input.tokenize(delimiter)
    return list
}

/*def is_valid_ssh_map (input, delimiter=':') {
    def name = false
    def state = false
    def key_pub = false
    list = tokenyze_input(input, ',')
    list.each {
        map = stringToArrayParser(it, delimiter, '\n')
        if ( ! map.isEmpty() ) {
            map.each{ key, value ->
                key = key.replaceAll("\\s","")
                if( key == 'name')
                    name = true
                if( key == 'state')
                    state = true
                if( key == 'key_pub')
                    key_pub = true
            }
        } 
    }
    if(name && state && key_pub)
        return true
    return false
}*/

def writeYml(yaml, yaml_path) {
    sh "rm $yaml_path"
    writeYaml file: yaml_path, data: yaml
    sh "cat $yaml_path"
}

def delete_entries_if_exists(yaml, keys_list, category, entry) {
    def index = 0
    keys_list.each { yml ->
        if(yaml.ssh.authorized_keys."${category}"[index].name == entry[0] && yaml.ssh.authorized_keys."${category}"[index].key_pub == entry[1])
            yaml.ssh.authorized_keys."${category}".removeAt(index)
        index ++
    }
    return yaml
}

def change_key_state_to_absent(yaml, keys_list, category, name) {
    def index = 0
    keys_list.each { yml ->
        if(name == yaml.ssh.authorized_keys."${category}"[index].name)
            yaml.ssh.authorized_keys."${category}"[index].state = "absent"
        index ++
    }
    return yaml
}

def delete_entries_if_state_is_absent(yaml, keys_list) {
    def index = 0
    keys_list.each {
        if(yaml.ssh.authorized_keys."${category}"[index].state == "absent")
            yaml.ssh.authorized_keys."${category}".removeAt(index)
        index ++
    }
    return yaml
}

def check_and_split_entry(entry) {
    key = entry.tokenize(':')
    //fields validity check (name:ssh_key)
    if ( key.size() != 2)
        sh "exit -1" 
    return key
}

def append_key_entry(yaml, key) {
    def ssh_keys = yaml.ssh.authorized_keys.devops + [ 'name': "${key[0]}", 'state': 'present', 'key_pub': "${key[1]}"]
    yaml.ssh.authorized_keys.devops = ssh_keys
    return yaml
}

return this

# ConfigUtil
Example config saver and loader for mc clients, but could be applied other places


## How to use

Include json in your gradle dependency block

```
dependencies {
    ...

    embed group: 'org.json', name: 'json', version: '20210307'
  ...

}
```

Then in your main class call the load method within the onInit function 

```ConfigUtil.loadConfig();```



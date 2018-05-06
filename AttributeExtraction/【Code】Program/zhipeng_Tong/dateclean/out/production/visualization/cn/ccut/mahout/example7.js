var tree_structure = {
    chart: {
        container: "#OrganiseChart6",
        levelSeparation:    20,
        siblingSeparation:  15,
        subTeeSeparation:   15,
        rootOrientation: "EAST",

        node: {
            HTMLclass: "tennis-draw",
            drawLineThrough: true
        },
        connectors: {
            type: "straight",
            style: {
                "stroke-width": 2,
                "stroke": "#ccc"
            }
        }
    },

    nodeStructure: { text: { name: {val: "属性:1"} }, HTMLclass: "winner"
        ,children: [
            { text: { name: "属性名:0", desc: "判别值:0" }
                ,children: [
                    { text: { name: "类型:0.0", desc: "判别值:0" } },
                    { text: { name: "类型:1.0", desc: "判别值:1" } } ] },
            { text: {  name: "属性名:1", desc: "判别值:0" }
                ,children: [
                    { text: { name: "类型:1.0", desc: "判别值:0" } },
                    { text: { name: "类型:1.0", desc: "判别值:1" } } ]
            }
        ]

    }
};

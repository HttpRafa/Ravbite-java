#version 400 core

in vec3 color;
in vec2 pass_textureCoords;

in vec3 surfaceNormal;
in vec3 toLightVector;
in vec3 toCameraVector;

out vec4 out_Color;

uniform sampler2D albedo_texture;

uniform vec3 lightColor;
uniform float shineDamper;
uniform float reflectivity;

void main(void) {

    vec3 unitNormal = normalize(surfaceNormal);
    vec3 unitLightVector = normalize(toLightVector);
    vec3 unitCameraVector = normalize(toCameraVector);

    float nDot1 = dot(unitNormal, unitLightVector);
    float brightness = max(nDot1, 0.2);
    vec3 diffuse = brightness * lightColor;

    vec3 lightDirection = -unitLightVector;
    vec3 reflectedLightDirection = reflect(lightDirection, unitNormal);

    float specularFactor = max(dot(reflectedLightDirection, unitCameraVector), 0.0);
    float dampedFactor = pow(specularFactor, shineDamper);

    vec3 finalSpecular = dampedFactor * reflectivity * lightColor;


    out_Color = vec4(diffuse, 1.0) * texture(albedo_texture, pass_textureCoords) + vec4(finalSpecular, 1.0);

}